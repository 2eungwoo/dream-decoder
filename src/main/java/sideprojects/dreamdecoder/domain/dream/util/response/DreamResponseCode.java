package sideprojects.dreamdecoder.domain.dream.util.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import sideprojects.dreamdecoder.global.shared.response.ResponseCode;

@Getter
@AllArgsConstructor
public enum DreamResponseCode implements ResponseCode {
    DREAM_SAVE_SUCCESS(HttpStatus.CREATED, "DRM001", "꿈 해몽 결과가 성공적으로 저장되었습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
