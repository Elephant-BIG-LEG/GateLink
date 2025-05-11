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
public class RequestMessageBody {
    private long associatedMsgId;     // 关联的消息ID（用于回复等场景）
    private String content;           // 文本内容
    private byte[] attachment;      // 附件（图片、文件等）
}