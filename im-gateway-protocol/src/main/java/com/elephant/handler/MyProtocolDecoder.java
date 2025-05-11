package com.elephant.handler;

import com.elephant.RequestMessage;
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
public class MyProtocolDecoder  extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        // 最小 header 大小判断（防止粘包）
        if (in.readableBytes() < 64) {
            return;
        }

        // Header 解析
        int magic = in.readInt();
        byte version = in.readByte();
        long msgId = in.readLong();
        long sessionId = in.readLong();
        long senderId = in.readLong();
        long receiverId = in.readLong();
        byte msgType = in.readByte();
        byte contentType = in.readByte();
        byte encType = in.readByte();
        int bodyLen = in.readInt();
        long timestamp = in.readLong();

        int chatSceneLen = in.readInt();
        byte[] chatSceneBytes = new byte[chatSceneLen];
        in.readBytes(chatSceneBytes);
        String chatScene = new String(chatSceneBytes, StandardCharsets.UTF_8);

        byte statusOrdinal = in.readByte();
        MessageStatus status = MessageStatus.values()[statusOrdinal];

        // Body 解析
        long associatedMsgId = in.readLong();

        int contentLen = in.readInt();
        byte[] contentBytes = new byte[contentLen];
        in.readBytes(contentBytes);
        String content = new String(contentBytes, StandardCharsets.UTF_8);

        int attachLen = in.readInt();
        byte[] attachment = new byte[attachLen];
        in.readBytes(attachment);

        // 封装对象
        RequestMessageHeader header = RequestMessageHeader.builder()
                .magicNumber(magic)
                .version(version)
                .msgId(msgId)
                .sessionId(sessionId)
                .senderId(senderId)
                .receiverId(receiverId)
                .messageType(msgType)
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
}