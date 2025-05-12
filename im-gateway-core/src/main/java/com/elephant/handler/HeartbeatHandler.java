package com.elephant.handler;

import com.elephant.RequestMessage;
import com.elephant.message.MessageType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.TimeUnit;


/**
 * @Author: Elephant-FZY
 * @Email: https://github.com/Elephant-BIG-LEG
 * @Date: 2025/05/11/15:16
 * @Description: TODO
 * @Function： 返回当前服务的性能情况【维护长连接的数量】、
 *            断连重连【如果发生了断连，开启一个超时计时器】、
 *
 *
 */
@Slf4j
public class HeartbeatHandler extends ChannelInboundHandlerAdapter {

    /**
     * 当连接建立时调用
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }


    /**
     * 当连接断开时调用
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        // 模拟断连后重连机制（实际中客户端应负责重连）
        reconnectLater(ctx);
        super.channelInactive(ctx);
    }

    // 模拟一个重连任务（服务端可选，一般由客户端做）

    /**
     * 重试机制
     * @param ctx
     */
    private void reconnectLater(ChannelHandlerContext ctx) {
        ctx.executor().schedule(() -> {
            log.info("尝试重连（模拟）...");
            // 实际重连逻辑通常在客户端发起，这里只是做演示
            // 比如可以通知其他模块或上报状态
        }, 5, TimeUnit.SECONDS);
    }


    // 可选：处理客户端的心跳包（如果有）
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof RequestMessage requestMessage) {
            if (requestMessage.getRequestMessageHeader().getMessageType() == MessageType.HEARTBEAT) {
                if(log.isDebugEnabled()){
                    log.debug("收到心跳包: {}", requestMessage);
                }
                // 如果需要，可以写回心跳响应
                return; // 拦截心跳，不传递下一个 handler
            }
        }
        // 非心跳消息继续传递
        super.channelRead(ctx, msg);
    }

    // 对外暴露接口：获取当前连接数
    public static int getActiveConnectionCount() {
        return ConnectionRegistryHandler.uidChannelMap.size();
    }


    // 捕获异常
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("发生异常：{}", cause.getMessage());
        ctx.close();
    }
}