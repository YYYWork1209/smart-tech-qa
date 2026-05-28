package com.ai.springai.enums;

import lombok.Getter;

@Getter
public enum MessagesType {

    USER("user"),
    ASSISTANT("assistant");

    private final String messagesType;

    private MessagesType(String messagesType){
        this.messagesType = messagesType;

    }
}
