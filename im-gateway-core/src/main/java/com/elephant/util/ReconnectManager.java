package com.elephant.util;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;
import java.util.concurrent.*;
import static com.elephant.handler.ConnectionRegistryHandler.UID_CHANNL_MAP;

/**
 * @Author: Elephant-FZY
 * @Email: https://github.com/Elephant-BIG-LEG
 * @Date: 2025/05/14/19:55
 * @Description: 应对弱网情况，对断连和重连状态的复用
 */
@Slf4j
public class ReconnectManager {

    // 设定超时时间，单位：分钟
    private static final long RECONNECT_TIMEOUT = 5L;
    private static final ScheduledExecutorService reconnectScheduler
            = Executors.newSingleThreadScheduledExecutor();

    // 重连任务映射（通过 UID + DID 来标识）
    private static final Map<String, ScheduledFuture<?>> reconnectTasks = new ConcurrentHashMap<>();

    public static void startReconnectTimer(Channel channel, String uid, String did) {
        String key = uid + did;

        // 如果已经有重连任务，则取消掉
        cancelReconnectTimer(key);

        // 启动一个新的重连任务
        ScheduledFuture<?> future = reconnectScheduler.schedule(() -> {
            // 超时后，清理资源
            if (!UID_CHANNL_MAP.containsKey(key)) {
                log.warn("用户 {} 的 DID {} 超时未重连，清理连接", uid, did);
                // 可以执行一些清理任务
                // 比如：回收资源，移除路由表等
            }
        }, RECONNECT_TIMEOUT, TimeUnit.MINUTES);

        // 将重连任务存入映射
        reconnectTasks.put(key, future);
    }

    public static void cancelReconnectTimer(String key) {
        ScheduledFuture<?> future = reconnectTasks.get(key);
        if (future != null) {
            future.cancel(false);
            reconnectTasks.remove(key);
        }
    }
}
