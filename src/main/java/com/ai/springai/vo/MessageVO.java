package com.ai.springai.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;

/**
 * 用来存储会话历史内容，返回给前端
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageVO {

    private String role;
    private String content;

    public MessageVO(Message message){
        switch (message.getMessageType()) {
            case USER -> this.role = "user";
            case ASSISTANT -> this.role = "assistant";
            default -> role = "";
        }

        this.content = message.getText();
    }

}
