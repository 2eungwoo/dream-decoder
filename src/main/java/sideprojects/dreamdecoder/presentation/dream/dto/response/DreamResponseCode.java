package sideprojects.dreamdecoder.presentation.dream.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import sideprojects.dreamdecoder.global.shared.response.ResponseCode;

@Getter
@AllArgsConstructor
public enum DreamResponseCode implements ResponseCode {
    DREAM_SAVE_SUCCESS(HttpStatus.CREATED, "DRM001", "꿈 해몽 결과 저장 성공"),
    DREAM_FOUND_ALL_SUCCESS(HttpStatus.OK, "DREAM_002", "꿈 목록 조회 성공"),
    DREAM_FOUND_SUCCESS(HttpStatus.OK, "DRM003", "꿈 해몽이 조회 성공");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
