package com.elephant.handler;

import com.elephant.RequestMessage;
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

    // UID + DID -> Channel 映射
    private static final Map<String, Channel> uidChannelMap = new ConcurrentHashMap<>();


    /**
     * 建立链接
     * @param ctx
     * @param msg
     * @throws UnknownHostException
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestMessage msg) throws UnknownHostException {
        // 解码后，从消息中获取用户 UID
        String uid = msg.getRequestMessageHeader().getSenderId();
        String did = msg.getRequestMessageHeader().getSenderDid();
        // 获取当前 Channel
        Channel channel = ctx.channel();

        // 打印连接信息
        if (log.isDebugEnabled()) {
            log.debug("新连接注册：UID={},DID={}", uid);
        }

        // 将 UID 和 Channel 存入注册表
        uidChannelMap.put(uid + did, channel);

        // 其他业务处理...
        // 例如：鉴权、验证等

        // 向下游传递消息（如果有其他 handler 需要处理）
        ctx.fireChannelRead(msg);
    }


    /**
     * 离线操作
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 连接断开时的清理工作
        Channel channel = ctx.channel();
        // 获取 UID（可以从 channel 中获取到或者在注册时存储到 Channel 的属性中）
        String uid = getUidFromChannel(channel);

        String did = getDidFromChannel(channel);
        // 从注册表中注销连接
        if (uid != null) {
            uidChannelMap.remove(uid + did);
            log.debug("连接已关闭，注销用户UID: {} 的DID:{} 设备", uid,did);
        }

        super.channelInactive(ctx);
    }

    // 可能需要这个方法来从 Channel 中获取 UID（假设你在注册时存储了它）
    private String getUidFromChannel(Channel channel) {
        // 根据你的逻辑返回对应的 UID
        // 例如，可以存储在 Channel 的属性中
        return (String) channel.attr(AttributeKey.valueOf("uid")).get();
    }

    private String getDidFromChannel(Channel channel) {
        // 根据你的逻辑返回对应的 UID
        // 例如，可以存储在 Channel 的属性中
        return (String) channel.attr(AttributeKey.valueOf("did")).get();
    }
}