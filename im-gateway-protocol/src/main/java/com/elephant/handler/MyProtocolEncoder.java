package com.elephant.handler;

import com.elephant.RequestMessage;
import com.elephant.ResponseMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.StandardCharsets;


/**
 * @Author: Elephant-FZY
 * @Email: https://github.com/Elephant-BIG-LEG
 * @Date: 2025/05/11/11:44
 * @Description: 出栈消息处理器
 */
public class MyProtocolEncoder extends MessageToByteEncoder<ResponseMessage> {
    @Override
    protected void encode(ChannelHandlerContext ctx, ResponseMessage msg, ByteBuf out) throws Exception {
        // 编码 msgId
        out.writeLong(msg.getMsgId());

        // 编码 sessionId
        out.writeLong(msg.getSessionId());

        // 编码 senderId
        out.writeLong(msg.getSenderId());

        // 编码消息类型
        out.writeByte(msg.getMessageType().ordinal());

        // 编码 messageStatus，假设是枚举类型，使用 ordinal() 获取对应的数字
        out.writeByte(msg.getMessageStatus().ordinal()); // 将枚举转化为 byte 类型
    }
}