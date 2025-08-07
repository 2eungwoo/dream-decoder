package sideprojects.dreamdecoder.domain.dream.util.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import sideprojects.dreamdecoder.global.shared.response.ErrorCode;

@Getter
@AllArgsConstructor
public enum DreamErrorCode implements ErrorCode {
    DREAM_NOT_FOUND(HttpStatus.NOT_FOUND, "D001", "꿈을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
