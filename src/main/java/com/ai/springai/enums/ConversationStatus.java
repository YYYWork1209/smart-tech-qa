package com.ai.springai.enums;

import lombok.Getter;

@Getter
public enum ConversationStatus {
    NORMAL(1),
    DELETED(0);

    private final Integer status;

    private ConversationStatus(Integer status){
        this.status = status;
    }

}
