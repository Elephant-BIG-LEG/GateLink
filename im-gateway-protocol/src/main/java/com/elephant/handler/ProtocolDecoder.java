package com.elephant.handler;

import com.elephant.Message;
import com.elephant.message.MessageBody;
import com.elephant.message.MessageHeader;
import com.elephant.message.MessageStatus;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * @Author: Elephant-FZY
 * @Email: https://github.com/Elephant-BIG-LEG
 * @Date: 2025/05/10/21:10
 * @Description: 消息解码器
 */
public class ProtocolDecoder {

    public Message decodeMessage(byte[] data) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        try (DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream)) {
            // 解码消息头部分
            MessageHeader messageHeader = decodeMessageHeader(dataInputStream);

            // 解码消息体部分
            MessageBody messageBody = decodeMessageBody(dataInputStream);

            return new Message(messageHeader, messageBody);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private MessageHeader decodeMessageHeader(DataInputStream dataInputStream) throws IOException {
        int magicNumber = dataInputStream.readInt();
        byte version = dataInputStream.readByte();
        long msgId = dataInputStream.readLong();
        long sessionId = dataInputStream.readLong();
        long senderId = dataInputStream.readLong();
        long receiverId = dataInputStream.readLong();
        byte messageType = dataInputStream.readByte();
        byte contentType = dataInputStream.readByte();
        byte encryptionType = dataInputStream.readByte();
        int bodyLength = dataInputStream.readInt();
        long timestamp = dataInputStream.readLong();
        String chatScene = dataInputStream.readUTF();
        String status = dataInputStream.readUTF();

        // 创建消息头对象
        MessageHeader messageHeader = new MessageHeader(magicNumber, version, msgId, sessionId,
                senderId, receiverId, messageType, contentType, encryptionType,
                bodyLength, timestamp, chatScene, MessageStatus.valueOf(status));

        return messageHeader;
    }

    private MessageBody decodeMessageBody(DataInputStream dataInputStream) throws IOException {
        long associatedMsgId = dataInputStream.readLong();
        String content = dataInputStream.readUTF();
        int attachmentLength = dataInputStream.readInt();
        byte[] attachment = null;

        if (attachmentLength > 0) {
            attachment = new byte[attachmentLength];
            dataInputStream.readFully(attachment);
        }

        return new MessageBody(associatedMsgId, content, attachment);
    }
}
