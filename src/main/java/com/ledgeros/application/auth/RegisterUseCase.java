package com.ledgeros.application.auth;

import com.ledgeros.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RegisterUseCase {
    private final UserRepository userRepository;


    public void execute(){

    }

    public RegisterUseCase() {
        this.userRepository = new UserRepository();
    }
}
