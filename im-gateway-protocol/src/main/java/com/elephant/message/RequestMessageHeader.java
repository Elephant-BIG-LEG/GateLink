package com.elephant.message;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestMessageHeader {
    private int magicNumber;             // 魔数，用于协议识别
    private byte version;                // 版本号
    private String msgId;                // 消息ID
    private String sessionId;            // 会话ID
    private String senderId;             // 发送者ID
    private String senderDid;            // 设备ID
    private String receiverId;           // 接收者ID
    private MessageType messageType;     // 消息类型
    private byte contentType;            // 内容类型（JSON/XML/自定义等）
    private byte encryptionType;         // 加密类型
    private int bodyLength;              // 消息体长度
    private long timestamp;              // 时间戳
    private String chatScene;            // 聊天场景（枚举）
    private MessageStatus status;        // 消息状态（发送中/已发送/已接收等）
}
