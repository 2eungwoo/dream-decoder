package sideprojects.dreamdecoder.application.web.auth.usecase;

import sideprojects.dreamdecoder.presentation.web.auth.dto.AuthResponse;
import sideprojects.dreamdecoder.presentation.web.auth.dto.SignUpRequest;

public interface SignUpUseCase {
    AuthResponse execute(SignUpRequest request);
}