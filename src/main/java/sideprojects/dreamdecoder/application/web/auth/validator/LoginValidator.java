package sideprojects.dreamdecoder.application.web.auth.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sideprojects.dreamdecoder.domain.auth.persistence.User;
import sideprojects.dreamdecoder.domain.auth.persistence.UserRepository;
import sideprojects.dreamdecoder.domain.auth.util.exception.AuthException;
import sideprojects.dreamdecoder.domain.auth.util.response.AuthErrorCode;
import sideprojects.dreamdecoder.application.web.auth.usecase.PasswordEncryptUseCase;
import sideprojects.dreamdecoder.presentation.web.auth.dto.LoginRequest;

@Component
@RequiredArgsConstructor
public class LoginValidator {

    private final UserRepository userRepository;
    private final PasswordEncryptUseCase passwordEncryptUseCase;

    public User validateAndGetUser(LoginRequest request) {
        User user = validateUserExists(request.getUsername());
        validatePassword(request.getPassword(), user.getPassword());
        return user;
    }

    private User validateUserExists(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new AuthException(AuthErrorCode.USER_NOT_FOUND));
    }

    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncryptUseCase.matches(rawPassword, encodedPassword)) {
            throw new AuthException(AuthErrorCode.INVALID_PASSWORD);
        }
    }
}
