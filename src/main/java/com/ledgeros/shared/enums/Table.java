package com.ledgeros.shared.enums;

import lombok.Getter;

@Getter
public enum Table {

    USERS("users", "USERS_TABLE_NAME"),
    REFRESH_TOKENS("refresh_tokens", "REFRESH_TOKENS_TABLE_NAME");

    private final String value;
    private final String envVarName;

    Table(String value, String envVarName) {
        this.value = value;
        this.envVarName = envVarName;
    }

    public String getTableName() {
        String envTableName = System.getenv(envVarName);
        if (envTableName != null && !envTableName.isBlank()) {
            return envTableName;
        }
        String stage = System.getenv().getOrDefault("STAGE", "dev");
        return "ledgeros-" + stage + "-" + value;
    }
}
