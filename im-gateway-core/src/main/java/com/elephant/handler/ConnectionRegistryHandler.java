package com.elephant.handler;

import com.elephant.RequestMessage;
import com.elephant.util.ReconnectManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Elephant-FZY
 * @Email: https://github.com/Elephant-BIG-LEG
 * @Date: 2025/05/11/12:16
 * @Description: 建立注册表
 */
@Slf4j
public class ConnectionRegistryHandler extends SimpleChannelInboundHandler<RequestMessage> {

    public static final Map<String, Channel> UID_CHANNL_MAP = new ConcurrentHashMap<>();

    /**
     *
     * @param ctx
     * @param msg
     * @throws UnknownHostException
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestMessage msg) throws UnknownHostException {
        String uid = msg.getRequestMessageHeader().getSenderId();
        String did = msg.getRequestMessageHeader().getSenderDid();
        Channel channel = ctx.channel();

        // 打印连接信息
        if (log.isDebugEnabled()) {
            log.debug("新连接注册：UID={},DID={}", uid, did);
        }

        String key = uid + did;

        if (UID_CHANNL_MAP.containsKey(key)) {
            // 客户端重新连接，复用之前的状态
            Channel previousChannel = UID_CHANNL_MAP.get(key);
            if (previousChannel != channel) {
                if (log.isDebugEnabled()){
                    log.debug("复用之前的状态，更新路由信息：UID={}, DID={}", uid, did);
                }
                // 这里只需要更新路由表或其它状态信息
                UID_CHANNL_MAP.put(key, channel);
                // 可以在这里执行其它业务，比如重用 session 信息等
            }
        } else {
            // 新连接，注册到映射
            UID_CHANNL_MAP.put(key, channel);
        }

        // 启动或取消重连计时器
        ReconnectManager.cancelReconnectTimer(key);
        ReconnectManager.startReconnectTimer(channel, uid, did);

        ctx.fireChannelRead(msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 连接断开时的清理工作
        Channel channel = ctx.channel();
        String uid = getUidFromChannel(channel);
        String did = getDidFromChannel(channel);

        // 关闭连接前启动重连计时器
        if (uid != null) {
            ReconnectManager.startReconnectTimer(channel, uid, did);
            UID_CHANNL_MAP.remove(uid + did);
            log.debug("连接已关闭，启动重连计时器：UID={}, DID={}", uid, did);
        }

        super.channelInactive(ctx);
    }

    private String getUidFromChannel(Channel channel) {
        return (String) channel.attr(AttributeKey.valueOf("uid")).get();
    }

    private String getDidFromChannel(Channel channel) {
        return (String) channel.attr(AttributeKey.valueOf("did")).get();
    }
}
