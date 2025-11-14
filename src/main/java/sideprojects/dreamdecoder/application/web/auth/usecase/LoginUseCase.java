package sideprojects.dreamdecoder.application.web.auth.usecase;

import sideprojects.dreamdecoder.presentation.web.auth.dto.AuthResponse;
import sideprojects.dreamdecoder.presentation.web.auth.dto.LoginRequest;

public interface LoginUseCase {
    AuthResponse execute(LoginRequest request);
}