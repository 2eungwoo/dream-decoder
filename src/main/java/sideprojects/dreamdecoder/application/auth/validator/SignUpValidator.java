package sideprojects.dreamdecoder.application.auth.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sideprojects.dreamdecoder.domain.auth.persistence.UserRepository;
import sideprojects.dreamdecoder.domain.auth.util.exception.AuthException;
import sideprojects.dreamdecoder.domain.auth.util.response.AuthErrorCode;
import sideprojects.dreamdecoder.presentation.auth.dto.SignUpRequest;

@Component
@RequiredArgsConstructor
public class SignUpValidator {

    private final UserRepository userRepository;

    public void validate(SignUpRequest request) {
        validatePasswordMatch(request.getPassword(), request.getConfirmPassword());
        validateUsernameDuplication(request.getUsername());
        validateEmailDuplication(request.getEmail());
    }

    private void validatePasswordMatch(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new AuthException(AuthErrorCode.PASSWORD_MISMATCH);
        }
    }

    private void validateUsernameDuplication(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new AuthException(AuthErrorCode.USERNAME_ALREADY_EXISTS);
        }
    }

    private void validateEmailDuplication(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new AuthException(AuthErrorCode.EMAIL_ALREADY_EXISTS);
        }
    }
}
