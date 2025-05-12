package com.elephant.handler;

import com.elephant.RequestMessage;
import com.elephant.message.MessageType;
import com.elephant.message.RequestMessageBody;
import com.elephant.message.RequestMessageHeader;
import com.elephant.message.MessageStatus;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
/**
 * @Author: Elephant-FZY
 * @Email: https://github.com/Elephant-BIG-LEG
 * @Date: 2025/05/11/11:44
 * @Description: 入栈消息处理器
 */
public class MyProtocolDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        // 最小 header 大小判断（防止粘包）
        if (in.readableBytes() < 80) {  // 由于字段类型改变，更新最小字节数
            return;
        }

        // Header 解析
        int magic = in.readInt();
        byte version = in.readByte();

        // 读取字符串字段
        String msgId = readString(in);
        String sessionId = readString(in);
        String senderId = readString(in);
        String senderDid = readString(in);
        String receiverId = readString(in);

        // 解析 messageType（枚举）
        byte msgTypeOrdinal = in.readByte();
        MessageType messageType = MessageType.values()[msgTypeOrdinal];
        byte contentType = in.readByte();
        byte encType = in.readByte();
        int bodyLen = in.readInt();
        long timestamp = in.readLong();

        // 解析 chatScene（字符串字段）
        String chatScene = readString(in);

        // 解析消息状态（枚举）
        byte statusOrdinal = in.readByte();
        MessageStatus status = MessageStatus.values()[statusOrdinal];

        // Body 解析
        String associatedMsgId = readString(in);
        String content = readString(in);
        byte[] attachment = readBytes(in);

        // 封装对象
        RequestMessageHeader header = RequestMessageHeader.builder()
                .magicNumber(magic)
                .version(version)
                .msgId(msgId)
                .sessionId(sessionId)
                .senderId(senderId)
                .senderDid(senderDid)
                .receiverId(receiverId)
                .messageType(messageType)
                .contentType(contentType)
                .encryptionType(encType)
                .bodyLength(bodyLen)
                .timestamp(timestamp)
                .chatScene(chatScene)
                .status(status)
                .build();

        RequestMessageBody body = new RequestMessageBody(associatedMsgId, content, attachment);

        out.add(new RequestMessage(header, body));
    }

    // 辅助方法：读取字符串
    private String readString(ByteBuf in) {
        int length = in.readInt();  // 假设字符串前有一个整型长度字段
        byte[] bytes = new byte[length];
        in.readBytes(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    // 辅助方法：读取字节数组
    private byte[] readBytes(ByteBuf in) {
        int length = in.readInt();
        byte[] bytes = new byte[length];
        in.readBytes(bytes);
        return bytes;
    }
}