package sideprojects.dreamdecoder.application.auth.service;

import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;
import sideprojects.dreamdecoder.application.auth.usecase.TokenGenerateUseCase;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;


@Component
public class TokenGenerateService implements TokenGenerateUseCase {

    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long EXPIRATION_TIME = 86400000; // 24시간

    @Override
    public String generateToken(String username) {
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(secretKey)
            .compact();
    }
}
