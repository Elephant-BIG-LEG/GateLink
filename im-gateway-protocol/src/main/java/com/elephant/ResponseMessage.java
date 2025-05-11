package com.elephant;

import com.elephant.message.MessageStatus;
import lombok.*;

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
public class ResponseMessage {
    private long msgId;                  // 消息ID
    private long sessionId;              // 会话ID
    private long senderId;               // 发送者ID
    private MessageStatus messageStatus; // 消息转发状态
}
