package sideprojects.dreamdecoder.application.cli.auth;

import static sideprojects.dreamdecoder.domain.auth.util.response.AuthErrorCode.PASSWORD_MISMATCH;

import org.springframework.stereotype.Component;

@Component
public class CliPasswordValidator {

    public void validatePassword(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new PasswordMissMatchException(PASSWORD_MISMATCH.getMessage(), PASSWORD_MISMATCH);
        }
    }
}