package sideprojects.dreamdecoder.application.auth.cli.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sideprojects.dreamdecoder.application.auth.cli.service.validator.CliPasswordValidator;
import sideprojects.dreamdecoder.application.auth.usecase.LoginUseCase;
import sideprojects.dreamdecoder.application.auth.usecase.SignUpUseCase;
import sideprojects.dreamdecoder.application.auth.validator.LoginValidator; // Use existing LoginValidator
import sideprojects.dreamdecoder.infrastructure.cli.TokenManager;
import sideprojects.dreamdecoder.infrastructure.security.TokenManager;
import sideprojects.dreamdecoder.presentation.auth.dto.AuthResponse;
import sideprojects.dreamdecoder.presentation.auth.dto.LoginRequest;
import sideprojects.dreamdecoder.presentation.auth.dto.SignUpRequest;

@Service
@RequiredArgsConstructor
public class CliAuthService {

    private final LoginUseCase loginUseCase;
    private final SignUpUseCase signUpUseCase;
    private final sideprojects.dreamdecoder.infrastructure.security.TokenManager tokenManager;
    private final CliPasswordValidator passwordValidator;

    public void login(String username, String password) {
        LoginRequest request = new LoginRequest(username, password);
        AuthResponse response = loginUseCase.execute(request);
        tokenManager.saveToken(response.token());
    }

    public void signUp(String username, String email, String password, String confirmPassword) {
        passwordValidator.validatePassword(password, confirmPassword);
        SignUpRequest request = new SignUpRequest(username, email, password, confirmPassword);
        signUpUseCase.execute(request);
    }

    public void logout() {
        tokenManager.deleteToken();
    }
}
