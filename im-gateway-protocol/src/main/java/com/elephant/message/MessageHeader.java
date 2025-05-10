package com.elephant.message;

import lombok.*;

/**
 * @Author: Elephant-FZY
 * @Email: https://github.com/Elephant-BIG-LEG
 * @Date: 2025/05/10/20:35
 * @Description: TODO
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageHeader {
    private int magicNumber;      // 魔数，用于协议识别
    private byte version;         // 版本号
    private long msgId;           // 消息ID
    private long sessionId;       // 会话ID
    private long senderId;        // 发送者ID
    private long receiverId;      // 接收者ID
    private byte messageType;     // 消息类型（文本/图片/视频等）
    private byte contentType;     // 内容类型（JSON/XML/自定义等）
    private byte encryptionType;  // 加密类型
    private int bodyLength;       // 消息体长度
    private long timestamp;       // 时间戳
    private String chatScene;     // 聊天场景（枚举）
    private MessageStatus status; // 消息状态（发送中/已发送/已接收等）
}
