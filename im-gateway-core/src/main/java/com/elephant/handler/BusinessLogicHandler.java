package com.elephant.handler;

import com.elephant.*;
import com.elephant.discovery.RegistryConfig;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Elephant-FZY
 * @Email: https://github.com/Elephant-BIG-LEG
 * @Date: 2025/05/11/12:15
 * @Description: 业务层 -- 作为RPC客户端 发送到后端服务器 -- 调用 MQ 的异步落库、三端的消息通讯
 */
@Slf4j
public class BusinessLogicHandler extends SimpleChannelInboundHandler<RequestMessage> {

    private final HelloYrpc helloYrpc;

    public BusinessLogicHandler(HelloYrpc helloYrpc) {
        this.helloYrpc = helloYrpc;
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestMessage requestMessage) throws Exception {
        // 你可以在这里调用RPC接口了
        String response = helloYrpc.sayHi("来自 IM系统 的调用");
        if (log.isDebugEnabled()){
            log.debug("RPC 返回结果: {}", response);
        }

        // 可以写回客户端
        ctx.writeAndFlush(response);
    }
}