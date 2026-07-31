package com.ledgeros.shared.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TokenType {
    BEARER("Bearer");

    private final String value;
}
