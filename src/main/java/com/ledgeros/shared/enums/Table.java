package com.ledgeros.shared.enums;

import lombok.Getter;

@Getter
public enum Table {
    USERS("users"),
    REFRESH_TOKENS("refresh_tokens");

    private final String value;
    Table(String value) {
        this.value = value;
    }
}
