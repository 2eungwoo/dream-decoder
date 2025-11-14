package sideprojects.dreamdecoder.application.web.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import sideprojects.dreamdecoder.application.web.auth.usecase.PasswordEncryptUseCase;

@Component
@RequiredArgsConstructor
public class PasswordEncryptService implements PasswordEncryptUseCase {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public String encrypt(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
