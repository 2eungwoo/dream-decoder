package sideprojects.dreamdecoder.application.auth.service;

import sideprojects.dreamdecoder.infrastructure.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sideprojects.dreamdecoder.domain.auth.model.UserModel;
import sideprojects.dreamdecoder.presentation.auth.dto.AuthResponse;
import sideprojects.dreamdecoder.domain.auth.persistence.User;
import sideprojects.dreamdecoder.application.auth.usecase.LoginUseCase;

import sideprojects.dreamdecoder.application.auth.validator.LoginValidator;
import sideprojects.dreamdecoder.presentation.auth.dto.LoginRequest;

@Service
@RequiredArgsConstructor
public class LoginService implements LoginUseCase {

    private final LoginValidator loginValidator;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public AuthResponse execute(LoginRequest request) {

        User user = loginValidator.validateAndGetUser(request);
        UserModel userModel = UserModel.from(user);
        String token = jwtTokenProvider.generateToken(userModel.getUsername());

        return AuthResponse.builder()
            .token(token)
            .username(user.getUsername())
            .email(user.getEmail())
            .build();
    }
}

