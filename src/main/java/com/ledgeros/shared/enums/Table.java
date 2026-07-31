package com.ledgeros.shared.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Table {

    USERS("users"),
    REFRESH_TOKENS("refresh_tokens");

    private final String value;

    public String getTableName(){
        String stage = System.getenv().getOrDefault("STAGE", "dev");
        return "ledgeros-" + stage + "-" + value;
    }
}
