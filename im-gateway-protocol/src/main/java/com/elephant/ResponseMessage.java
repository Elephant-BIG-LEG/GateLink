package com.elephant;

import com.elephant.message.MessageStatus;
import com.elephant.message.MessageType;
import com.elephant.message.RequestMessageHeader;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.UUID;

/**
 * @Author: Elephant-FZY
 * @Email: https://github.com/Elephant-BIG-LEG
 * @Date: 2025/05/11/11:58
 * @Description: TODO
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseMessage {
    private long msgId;                  // 消息ID
    private long sessionId;              // 会话ID
    private long senderId;               // 发送者ID
    private MessageStatus messageStatus; // 消息转发状态
    private MessageType messageType;// 消息类型

}
