package sideprojects.dreamdecoder.application.web.dream.util.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import sideprojects.dreamdecoder.global.shared.response.ErrorCode;

@Getter
@AllArgsConstructor
public enum CacheErrorCode implements ErrorCode {

    CACHE_DATA_NOT_FOUND(HttpStatus.NOT_FOUND, "CACHE_E001", "캐시된 데이터를 찾을 수 없거나 만료되었습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
