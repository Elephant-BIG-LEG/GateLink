package com.elephant.handler;

import com.elephant.Message;
import com.elephant.message.MessageBody;
import com.elephant.message.MessageHeader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @Author: Elephant-FZY
 * @Email: https://github.com/Elephant-BIG-LEG
 * @Date: 2025/05/10/21:10
 * @Description: 消息编码器
 */
public class ProtocolCodec {

    public byte[] encodeMessage(Message message) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream)) {
            // 编码固定头部分
            encodeMessageHeader(message.getMessageHeader(), dataOutputStream);

            // 编码消息体部分
            encodeMessageBody(message.getMessageBody(), dataOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream.toByteArray();
    }

    private void encodeMessageHeader(MessageHeader messageHeader, DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeInt(messageHeader.getMagicNumber());
        dataOutputStream.writeByte(messageHeader.getVersion());
        dataOutputStream.writeLong(messageHeader.getMsgId());
        dataOutputStream.writeLong(messageHeader.getSessionId());
        dataOutputStream.writeLong(messageHeader.getSenderId());
        dataOutputStream.writeLong(messageHeader.getReceiverId());
        dataOutputStream.writeByte(messageHeader.getMessageType());
        dataOutputStream.writeByte(messageHeader.getContentType());
        dataOutputStream.writeByte(messageHeader.getEncryptionType());
        dataOutputStream.writeInt(messageHeader.getBodyLength());
        dataOutputStream.writeLong(messageHeader.getTimestamp());
        dataOutputStream.writeUTF(messageHeader.getChatScene());
        dataOutputStream.writeUTF(messageHeader.getStatus().toString());
    }

    private void encodeMessageBody(MessageBody messageBody, DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeLong(messageBody.getAssociatedMsgId());
        dataOutputStream.writeUTF(messageBody.getContent());
        if (messageBody.getAttachment() != null) {
            dataOutputStream.writeInt(messageBody.getAttachment().length);
            dataOutputStream.write(messageBody.getAttachment());
        } else {
            dataOutputStream.writeInt(0);
        }
    }
}

