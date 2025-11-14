package sideprojects.dreamdecoder.infrastructure.security.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import sideprojects.dreamdecoder.global.shared.response.ErrorCode;

@Getter
@AllArgsConstructor
public enum TokenErrorCode implements ErrorCode {

    TOKEN_DIR_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "TOKEN_E001", "토큰 디렉토리 생성에 실패했습니다."),
    TOKEN_SAVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "TOKEN_E002", "토큰 저장에 실패했습니다."),
    TOKEN_READ_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "TOKEN_E003", "토큰 읽기에 실패했습니다."),
    TOKEN_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "TOKEN_E004", "토큰 삭제에 실패했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
