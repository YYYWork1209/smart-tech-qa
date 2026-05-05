package com.ai.springai.enums;

import lombok.Data;
import lombok.Getter;


@Getter
public enum SessionType {

    CHAT("chat"),
    SERVICE("service");

    private final String SessionType;

    private SessionType(String type){
        this.SessionType = type;
    }

}
