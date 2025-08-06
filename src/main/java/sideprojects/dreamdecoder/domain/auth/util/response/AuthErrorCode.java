package sideprojects.dreamdecoder.domain.auth.util.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import sideprojects.dreamdecoder.global.shared.response.ErrorCode;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "AUTH_E001", "비밀번호와 비밀번호 확인이 일치하지 않습니다."),
    USERNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "AUTH_E002", "이미 존재하는 사용자명입니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "AUTH_E003", "이미 존재하는 이메일입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "AUTH_E004", "존재하지 않는 사용자입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "AUTH_E005", "비밀번호가 일치하지 않습니다.");
    
    private final HttpStatus status;
    private final String code;
    private final String message;
}