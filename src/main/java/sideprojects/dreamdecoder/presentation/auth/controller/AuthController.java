package sideprojects.dreamdecoder.presentation.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sideprojects.dreamdecoder.presentation.auth.dto.AuthResponse;
import sideprojects.dreamdecoder.presentation.auth.dto.LoginRequest;
import sideprojects.dreamdecoder.domain.auth.util.response.AuthResponseCode;
import sideprojects.dreamdecoder.application.auth.usecase.LoginUseCase;
import sideprojects.dreamdecoder.application.auth.usecase.SignUpUseCase;
import sideprojects.dreamdecoder.global.shared.response.ApiResponse;
import sideprojects.dreamdecoder.presentation.auth.dto.SignUpRequest;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final SignUpUseCase signUpUseCase;
    private final LoginUseCase loginUseCase;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<AuthResponse>> signUp(@RequestBody @Valid SignUpRequest request) {
        AuthResponse response = signUpUseCase.execute(request);
        return ApiResponse.success(AuthResponseCode.SIGNUP_SUCCESS, response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody @Valid LoginRequest request) {
        AuthResponse response = loginUseCase.execute(request);
        return ApiResponse.success(AuthResponseCode.LOGIN_SUCCESS, response);
    }
}
