package sideprojects.dreamdecoder.application.auth.usecase;

public interface PasswordEncryptUseCase {
    String encrypt(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}