package sideprojects.dreamdecoder.application.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sideprojects.dreamdecoder.domain.auth.model.UserModel;
import sideprojects.dreamdecoder.presentation.auth.dto.AuthResponse;
import sideprojects.dreamdecoder.domain.auth.persistence.User;
import sideprojects.dreamdecoder.application.auth.usecase.LoginUseCase;
import sideprojects.dreamdecoder.application.auth.usecase.TokenGenerateUseCase;
import sideprojects.dreamdecoder.application.auth.validator.LoginValidator;
import sideprojects.dreamdecoder.presentation.auth.dto.LoginRequest;

@Service
@RequiredArgsConstructor
public class LoginService implements LoginUseCase {

    private final LoginValidator loginValidator;
    private final TokenGenerateUseCase tokenGenerateUseCase;

    @Override
    public AuthResponse execute(LoginRequest request) {

        User user = loginValidator.validateAndGetUser(request);
        UserModel userModel = UserModel.from(user);
        String token = tokenGenerateUseCase.generateToken(userModel.getUsername());

        return AuthResponse.builder()
            .token(token)
            .username(user.getUsername())
            .email(user.getEmail())
            .build();
    }
}

