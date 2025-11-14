package sideprojects.dreamdecoder.infrastructure.security;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import sideprojects.dreamdecoder.infrastructure.security.exception.TokenException;
import sideprojects.dreamdecoder.infrastructure.security.response.TokenErrorCode;

@Component
public class TokenManager {

    private final Path tokenFilePath;

    public TokenManager() {
        String homeDirectory = System.getProperty("user.home");
        Path tokenDirectory = Paths.get(homeDirectory, ".dream-decoder");
        if (!Files.exists(tokenDirectory)) {
            try {
                Files.createDirectories(tokenDirectory);
            } catch (IOException e) {
                throw new TokenException(TokenErrorCode.TOKEN_DIR_CREATION_FAILED);
            }
        }
        this.tokenFilePath = tokenDirectory.resolve("token");
    }

    public void saveToken(String token) {
        try {
            Files.writeString(tokenFilePath, token, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new TokenException(TokenErrorCode.TOKEN_SAVE_FAILED);
        }
    }

    public Optional<String> getToken() {
        if (!Files.exists(tokenFilePath)) {
            return Optional.empty();
        }
        try {
            return Optional.of(Files.readString(tokenFilePath, StandardCharsets.UTF_8).trim());
        } catch (IOException e) {
            throw new TokenException(TokenErrorCode.TOKEN_READ_FAILED);
        }
    }

    public void deleteToken() {
        try {
            Files.deleteIfExists(tokenFilePath);
        } catch (IOException e) {
            throw new TokenException(TokenErrorCode.TOKEN_DELETE_FAILED);
        }
    }
}