package com.elephant;

import com.elephant.handler.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Elephant-FZY
 * @Email: https://github.com/Elephant-BIG-LEG
 * @Date: 2025/05/11/11:29
 * @Description: TODO
 */
public class NettyServer {

    // TODO 线程模型优化

    /**
     * 在当前网管机器上建立长连接
     * @param port 服务端监听的端口
     */
    public void start(int port){
        EventLoopGroup bossGroup = new NioEventLoopGroup(2);
        EventLoopGroup workGroup = new NioEventLoopGroup(10);

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    // 解码
                                    .addLast(new MyProtocolDecoder())
                                    // TODO 登录校验【不是本项目重点，不做考虑】
                                    .addLast(null)
                                    // 建立注册表
                                    .addLast(new ConnectionRegistryHandler())
                                    // TODO 心跳
                                    .addLast(new HeartbeatHandler())
                                    // 限流
                                    //.addLast(new RateLimiterHandler())
                                    // TODO 下发到业务层中
                                    .addLast(new BusinessLogicHandler())
                                    // 编码 -- 响应
                                    .addLast(new MyProtocolEncoder());
                        }
                    });

            //绑定端口
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            channelFuture.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new NettyServer().start(8000);
    }
}
