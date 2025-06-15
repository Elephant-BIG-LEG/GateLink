package com.elephant;

import com.elephant.message.MessageStatus;
import com.elephant.message.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Elephant-FZY
 * @Email: https://github.com/Elephant-BIG-LEG
 * @Date: 2025/05/11/12:15
 * @Description: 响应消息类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMessage {
    private Long MsgId;
    private Long SessionId;
    private Long SenderId;
    private MessageType messageType;
    private MessageStatus messageStatus;
}
