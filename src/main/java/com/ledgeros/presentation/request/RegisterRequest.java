package com.ledgeros.presentation.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record RegisterRequest(
        String name,
        String email,
        String password
) {
    @JsonCreator
    public RegisterRequest(
            @JsonProperty("name") String name,
            @JsonProperty("email") String email,
            @JsonProperty("password") String password
    ) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
