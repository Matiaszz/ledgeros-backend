package com.ledgeros.shared.enums;

import lombok.Getter;

@Getter
public enum Table {
    USERS("users");

    private final String value;
    Table(String value) {
        this.value = value;
    }
}
