package com.ledgeros.application;

import com.ledgeros.application.auth.*;
import com.ledgeros.infrastructure.exception.ExceptionCode;
import com.ledgeros.infrastructure.exception.LambdaException;
import com.ledgeros.mocks.repositories.MockRefreshTokenRepository;
import com.ledgeros.mocks.repositories.MockUserRepository;
import com.ledgeros.presentation.request.*;
import com.ledgeros.presentation.response.*;
import com.ledgeros.shared.utils.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthSystemTest {

    private MockUserRepository userRepository;
    private MockRefreshTokenRepository refreshTokenRepository;
    private RegisterUseCase registerUseCase;
    private VerifyEmailUseCase verifyEmailUseCase;
    private LoginUseCase loginUseCase;
    private LogoutUseCase logoutUseCase;
    private ForgotPasswordUseCase forgotPasswordUseCase;
    private ResetPasswordUseCase resetPasswordUseCase;

    @BeforeEach
    public void setUp() {
        userRepository = new MockUserRepository();
        refreshTokenRepository = new MockRefreshTokenRepository();
        registerUseCase = new RegisterUseCase(userRepository);
        verifyEmailUseCase = new VerifyEmailUseCase(userRepository);
        loginUseCase = new LoginUseCase(userRepository, new JwtUtils(refreshTokenRepository));
        logoutUseCase = new LogoutUseCase(refreshTokenRepository);
        forgotPasswordUseCase = new ForgotPasswordUseCase(userRepository);
        resetPasswordUseCase = new ResetPasswordUseCase(userRepository);
    }

    @Test
    public void testFullAuthenticationLifecycle() {
        String name = "Test User";
        String email = "testuser@ledgeros.dev";
        String initialPassword = "SecurePassword123!";

        // 1. Register User
        RegisterRequest registerReq = new RegisterRequest(name, email, initialPassword);
        RegisterResponse registerRes = registerUseCase.execute(registerReq);

        assertNotNull(registerRes);
        assertEquals(email, registerRes.email());
        assertFalse(registerRes.emailVerified());
        assertNotNull(registerRes.verificationCode());

        String verificationCode = registerRes.verificationCode();

        // 2. Login BEFORE verification should FAIL with EMAIL_NOT_VERIFIED exception
        LoginRequest unverifiedLoginReq = new LoginRequest(email, initialPassword);
        LambdaException unverifiedEx = assertThrows(LambdaException.class, () -> loginUseCase.execute(unverifiedLoginReq));
        assertEquals(ExceptionCode.EMAIL_NOT_VERIFIED, unverifiedEx.getExceptionCode());
        assertEquals(403, unverifiedEx.getStatusCode());

        // 3. Verify Email
        VerifyEmailRequest verifyReq = new VerifyEmailRequest(email, verificationCode);
        MessageResponse verifyRes = verifyEmailUseCase.execute(verifyReq);
        assertNotNull(verifyRes);
        assertTrue(verifyRes.message().contains("successfully"));

        // 4. Login AFTER verification should SUCCEED
        LoginRequest verifiedLoginReq = new LoginRequest(email, initialPassword);
        TokenResponse tokenRes = loginUseCase.execute(verifiedLoginReq);
        assertNotNull(tokenRes);
        assertNotNull(tokenRes.accessToken());
        assertNotNull(tokenRes.generatedRefreshToken());

        // 4b. Logout
        LogoutRequest logoutReq = new LogoutRequest(tokenRes.generatedRefreshToken().token().id(), null);
        MessageResponse logoutRes = logoutUseCase.execute(logoutReq);
        assertNotNull(logoutRes);
        assertTrue(logoutRes.message().contains("successfully"));

        // 5. Forgot Password Request
        ForgotPasswordRequest forgotReq = new ForgotPasswordRequest(email);
        ForgotPasswordResponse forgotRes = forgotPasswordUseCase.execute(forgotReq);
        assertNotNull(forgotRes);
        assertNotNull(forgotRes.resetCode());

        String resetCode = forgotRes.resetCode();

        // 6. Reset Password
        String newPassword = "NewSecretPassword456!";
        ResetPasswordRequest resetReq = new ResetPasswordRequest(email, resetCode, newPassword);
        MessageResponse resetRes = resetPasswordUseCase.execute(resetReq);
        assertNotNull(resetRes);

        // 7. Login with OLD password should FAIL
        LoginRequest oldPassLoginReq = new LoginRequest(email, initialPassword);
        LambdaException oldPassEx = assertThrows(LambdaException.class, () -> loginUseCase.execute(oldPassLoginReq));
        assertEquals(ExceptionCode.INVALID_CREDENTIALS, oldPassEx.getExceptionCode());

        // 8. Login with NEW password should SUCCEED
        LoginRequest newPassLoginReq = new LoginRequest(email, newPassword);
        TokenResponse newPassTokenRes = loginUseCase.execute(newPassLoginReq);
        assertNotNull(newPassTokenRes);
        assertNotNull(newPassTokenRes.accessToken());
    }
}
