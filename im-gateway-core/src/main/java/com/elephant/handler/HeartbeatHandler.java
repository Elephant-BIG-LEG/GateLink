package com.elephant.handler;

import com.elephant.RequestMessage;
import com.elephant.ResponseMessage;
import com.elephant.message.MessageStatus;
import com.elephant.message.MessageType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ScheduledFuture;


/**
 * @Author: Elephant-FZY
 * @Email: https://github.com/Elephant-BIG-LEG
 * @Date: 2025/05/11/15:16
 * @Description: TODO
 * @Function： 返回当前服务的性能情况【维护长连接的数量】、
 *             探活
 *             断连重连【如果发生了断连，开启一个超时计时器】、
 */
@Slf4j
public class HeartbeatHandler extends ChannelInboundHandlerAdapter {

    // 四分钟
    private static final long SERVER_HEARTBEAT_INTERVAL = 4L;
    // 定时任务
    private ScheduledFuture<?> heartbeatTask;


    /**
     * 连接时调用
     * 开启探活
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if (log.isDebugEnabled()){
            log.info("客户端连接建立：{}", ctx.channel().remoteAddress());
        }

        // 启动服务端心跳发送任务
        heartbeatTask = ctx.executor().scheduleAtFixedRate(() -> {
            // 构建心跳包
            ResponseMessage heartbeat = ResponseMessage.builder()
                            .messageType(MessageType.HEARTBEAT).build();
            ctx.writeAndFlush(heartbeat);
            if(log.isDebugEnabled()){
                log.debug("服务端向客户端 {} 发送心跳包", ctx.channel().remoteAddress());
            }
        }, 0, SERVER_HEARTBEAT_INTERVAL, TimeUnit.MINUTES);

        super.channelActive(ctx);
    }

    /**
     * 下线时调用
     * 停止定时任务
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (log.isDebugEnabled()){
            log.info("客户端断开：{}", ctx.channel().remoteAddress());
        }

        if (heartbeatTask != null) {
            heartbeatTask.cancel(true); // 停止定时任务
        }

        super.channelInactive(ctx);
    }

    /**
     * 读取数据
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof RequestMessage requestMessage) {
            MessageType type = requestMessage.getRequestMessageHeader().getMessageType();

            if (type == MessageType.HEARTBEAT) {
                log.debug("收到客户端心跳包: {}", requestMessage);
                return; // 不向下传递
            } else if (type == MessageType.SERVICE_STATE) {
                int onlineCount = ConnectionRegistryHandler.UID_CHANNL_MAP.size();

                // 构建响应消息
                ResponseMessage response = new ResponseMessage();
                response.setMessageStatus(MessageStatus.SUCCESS);
                response.setMessageType(MessageType.SERVICE_STATE);
                // 转发
                ctx.writeAndFlush(response);
                return;
            }
        }

        super.channelRead(ctx, msg); // 非特殊消息，继续传递
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent idleEvent) {
            if (idleEvent.state() == IdleState.READER_IDLE) {
                log.warn("客户端 {} 超过2小时未发心跳或消息，主动断开", ctx.channel().remoteAddress());
                ctx.close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (log.isDebugEnabled()){
            log.error("异常：{}", cause.getMessage());
        }
        ctx.close();
    }
}