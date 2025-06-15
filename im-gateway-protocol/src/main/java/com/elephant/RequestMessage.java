package com.elephant;

import com.elephant.message.RequestMessageBody;
import com.elephant.message.RequestMessageHeader;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Elephant-FZY
 * @Email: https://github.com/Elephant-BIG-LEG
 * @Date: 2025/05/11/12:15
 * @Description: 请求消息类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestMessage {
    private RequestMessageHeader requestMessageHeader;
    private RequestMessageBody requestMessageBody;
}
