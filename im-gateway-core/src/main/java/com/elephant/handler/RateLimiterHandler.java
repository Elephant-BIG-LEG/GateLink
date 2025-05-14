package com.elephant.handler;

import com.elephant.RequestMessage;
import com.google.common.util.concurrent.RateLimiter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Elephant-FZY
 * @Email: https://github.com/Elephant-BIG-LEG
 * @Date: 2025/05/14/20:03
 * @Description: 限流器。用户粒度和全局粒度
 */
@Slf4j
public class RateLimiterHandler extends SimpleChannelInboundHandler<RequestMessage> {

    // 每秒 10 条
    private static final int PER_USER_RATE = 10;
    // 全局 QPS 限制
    private static final int GLOBAL_QPS = 20000;
    // 窗口大小（1秒）
    private static final int WINDOW_MS = 1000;

    private static final Map<String, RateLimiter> LOCAL_USER_LIMITERS = new ConcurrentHashMap<>();

    private static final JedisPool JEDIS_POOL = new JedisPool();
    private static final String LUA_SCRIPT;

    static {
        try {
            // 脚本路径
            LUA_SCRIPT = Files.readString(
                    Paths.get("im-gateway-core/src/main/resources/limit.lua"));
        } catch (IOException e) {
            throw new RuntimeException("加载Lua脚本失败", e);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestMessage msg) throws Exception {
        String uid = msg.getRequestMessageHeader().getSenderId();
        String did = msg.getRequestMessageHeader().getSenderDid();
        String key = uid + ":" + did;

        // 1. 用户级别令牌桶限流
        boolean pass = LOCAL_USER_LIMITERS
                .computeIfAbsent(key, k -> RateLimiter.create(PER_USER_RATE))
                .tryAcquire();

        if (!pass) {
            log.warn("用户 [{}] 被限流（每秒不超过 {} 条）", key, PER_USER_RATE);
            return;
        }

        // 2. Redis 全局限流
        if (!checkGlobalRateLimit()) {
            log.warn("全局限流触发，丢弃用户 [{}] 的请求", key);
            return;
        }

        // 3. 限流通过，继续向下处理
        ctx.fireChannelRead(msg);
    }

    /**
     * Jedis + Lua 脚本 进行限流
     * @return
     */
    private boolean checkGlobalRateLimit() {
        try (Jedis jedis = JEDIS_POOL.getResource()) {
            long now = System.currentTimeMillis();
            Object result = jedis.eval(LUA_SCRIPT,
                    Collections.singletonList("global:rate:limit"),
                    List.of(String.valueOf(GLOBAL_QPS),
                            String.valueOf(now),
                            String.valueOf(WINDOW_MS)));

            return Integer.parseInt(result.toString()) == 1;
        } catch (Exception e) {
            log.error("Redis限流异常，默认不放行", e);
            return false; // 限流异常时默认不放行
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("RateLimiterHandler 异常", cause);
        ctx.close();
    }
}
