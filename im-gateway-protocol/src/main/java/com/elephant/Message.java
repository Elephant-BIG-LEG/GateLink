package com.elephant;

import com.elephant.message.MessageBody;
import com.elephant.message.MessageHeader;
import lombok.*;

/**
 * @Author: Elephant-FZY
 * @Email: https://github.com/Elephant-BIG-LEG
 * @Date: 2025/05/10/20:43
 * @Description: 消息结构体
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private MessageHeader messageHeader;
    private MessageBody messageBody;
}
