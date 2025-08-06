package sideprojects.dreamdecoder.application.auth.usecase;

import sideprojects.dreamdecoder.presentation.auth.dto.AuthResponse;
import sideprojects.dreamdecoder.presentation.auth.dto.LoginRequest;

public interface LoginUseCase {
    AuthResponse execute(LoginRequest request);
}