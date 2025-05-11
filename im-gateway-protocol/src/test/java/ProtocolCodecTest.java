/**
 * @Author: Elephant-FZY
 * @Email: https://github.com/Elephant-BIG-LEG
 * @Date: 2025/05/10/21:13
 * @Description: TODO
 */

public class ProtocolCodecTest {
//
//    // 测试编码和解码过程
//    @Test
//    public void testEncodeDecodeMessage() {
//        // 创建消息头
//        MessageHeader messageHeader = new MessageHeader(
//                1234, // magicNumber
//                (byte) 1, // version
//                123456789L, // msgId
//                987654321L, // sessionId
//                111L, // senderId
//                222L, // receiverId
//                (byte) 0, // messageType (文本)
//                (byte) 1, // contentType (JSON)
//                (byte) 0, // encryptionType
//                50, // bodyLength
//                System.currentTimeMillis(), // timestamp
//                "general", // chatScene
//                MessageStatus.SUCCEED // status
//        );
//
//        // 创建消息体
//        MessageBody messageBody = new MessageBody(
//                0L, // associatedMsgId
//                "Hello, world!", // content
//                null // 没有附件
//        );
//
//        // 创建消息
//        Message message = new Message(messageHeader, messageBody);
//
//        System.out.println(message.getMessageBody().toString());
//
//        // 编码消息
//        ProtocolCodec codec = new ProtocolCodec();
//        byte[] encodedMessage = codec.encodeMessage(message);
//
//        // 解码消息
//        ProtocolDecoder decoder = new ProtocolDecoder();
//        Message decodedMessage = decoder.decodeMessage(encodedMessage);
//
//        System.out.println(decodedMessage);
//
//        // 验证解码后的消息与原始消息一致
//        assertNotNull(decodedMessage);
//        assertEquals(messageHeader.getMagicNumber(), decodedMessage.getMessageHeader().getMagicNumber());
//        assertEquals(messageHeader.getVersion(), decodedMessage.getMessageHeader().getVersion());
//        assertEquals(messageHeader.getMsgId(), decodedMessage.getMessageHeader().getMsgId());
//        assertEquals(messageHeader.getSessionId(), decodedMessage.getMessageHeader().getSessionId());
//        assertEquals(messageHeader.getSenderId(), decodedMessage.getMessageHeader().getSenderId());
//        assertEquals(messageHeader.getReceiverId(), decodedMessage.getMessageHeader().getReceiverId());
//        assertEquals(messageHeader.getMessageType(), decodedMessage.getMessageHeader().getMessageType());
//        assertEquals(messageHeader.getContentType(), decodedMessage.getMessageHeader().getContentType());
//        assertEquals(messageHeader.getEncryptionType(), decodedMessage.getMessageHeader().getEncryptionType());
//        assertEquals(messageHeader.getBodyLength(), decodedMessage.getMessageHeader().getBodyLength());
//        assertEquals(messageHeader.getTimestamp(), decodedMessage.getMessageHeader().getTimestamp());
//        assertEquals(messageHeader.getChatScene(), decodedMessage.getMessageHeader().getChatScene());
//        assertEquals(messageHeader.getStatus(), decodedMessage.getMessageHeader().getStatus());
//
//        assertEquals(messageBody.getAssociatedMsgId(), decodedMessage.getMessageBody().getAssociatedMsgId());
//        assertEquals(messageBody.getContent(), decodedMessage.getMessageBody().getContent());
//    }
//
//    // 测试编码消息时附件处理
//    @Test
//    public void testEncodeDecodeMessageWithAttachment() {
//        // 创建消息头
//        MessageHeader messageHeader = new MessageHeader(
//                1234, (byte) 1, 123456789L, 987654321L, 111L, 222L,
//                (byte) 0, (byte) 1, (byte) 0, 60, System.currentTimeMillis(),
//                "group", MessageStatus.SUCCEED
//        );
//
//        // 创建消息体（带附件）
//        byte[] attachment = new byte[] {1, 2, 3, 4, 5}; // 示例附件
//        MessageBody messageBody = new MessageBody(0L, "Hello, world with attachment!", attachment);
//
//        // 创建消息
//        Message message = new Message(messageHeader, messageBody);
//
//        System.out.println(message.toString());
//
//
//        // 编码消息
//        ProtocolCodec codec = new ProtocolCodec();
//        byte[] encodedMessage = codec.encodeMessage(message);
//
//        // 解码消息
//        ProtocolDecoder decoder = new ProtocolDecoder();
//        Message decodedMessage = decoder.decodeMessage(encodedMessage);
//
//        System.out.println(decodedMessage);
//
//        // 验证解码后的消息与原始消息一致
//        assertNotNull(decodedMessage);
//        assertArrayEquals(attachment, decodedMessage.getMessageBody().getAttachment());
//    }
}