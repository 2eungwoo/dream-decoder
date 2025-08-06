package sideprojects.dreamdecoder.domain.auth.util.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import sideprojects.dreamdecoder.global.shared.response.ResponseCode;

@Getter
@AllArgsConstructor
public enum AuthResponseCode implements ResponseCode {
    
    SIGNUP_SUCCESS(HttpStatus.CREATED, "AUTH_001", "회원가입이 성공적으로 완료되었습니다."),
    LOGIN_SUCCESS(HttpStatus.OK, "AUTH_002", "로그인이 성공적으로 완료되었습니다.");
    
    private final HttpStatus status;
    private final String code;
    private final String message;
}