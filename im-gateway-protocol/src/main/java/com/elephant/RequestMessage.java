package com.elephant;

import com.elephant.message.MessageType;
import com.elephant.message.RequestMessageBody;
import com.elephant.message.RequestMessageHeader;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.UUID;

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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestMessage {
    private RequestMessageHeader requestMessageHeader;
    private RequestMessageBody messageBody;
}
