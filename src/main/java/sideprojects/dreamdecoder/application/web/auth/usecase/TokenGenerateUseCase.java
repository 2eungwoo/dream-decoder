package sideprojects.dreamdecoder.application.web.auth.usecase;

public interface TokenGenerateUseCase {
    String generateToken(String username);
}
