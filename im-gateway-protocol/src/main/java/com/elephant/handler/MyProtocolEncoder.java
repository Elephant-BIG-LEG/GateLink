package com.elephant.handler;

import com.elephant.ResponseMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;


/**
 * @Author: Elephant-FZY
 * @Email: https://github.com/Elephant-BIG-LEG
 * @Date: 2025/05/11/11:44
 * @Description: 出栈消息处理器
 */
public class MyProtocolEncoder extends MessageToByteEncoder<ResponseMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ResponseMessage msg, ByteBuf out) {
        // 自定义协议（简洁响应结构）
        out.writeInt(0xCAFEBABE); // magic number
        out.writeByte(1);         // version
        out.writeLong(msg.getMsgId());
        out.writeLong(msg.getSessionId());
        out.writeLong(msg.getSenderId());
        out.writeByte(msg.getMessageStatus().ordinal()); // 枚举状态压缩成一个字节
    }
}