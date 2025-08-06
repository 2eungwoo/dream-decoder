package sideprojects.dreamdecoder.application.auth.usecase;

import sideprojects.dreamdecoder.presentation.auth.dto.AuthResponse;
import sideprojects.dreamdecoder.presentation.auth.dto.SignUpRequest;

public interface SignUpUseCase {
    AuthResponse execute(SignUpRequest request);
}