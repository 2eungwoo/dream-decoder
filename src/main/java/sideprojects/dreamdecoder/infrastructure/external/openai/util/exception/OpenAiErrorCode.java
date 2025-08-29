package sideprojects.dreamdecoder.infrastructure.external.openai.util.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import sideprojects.dreamdecoder.global.shared.response.ErrorCode;

@Getter
@AllArgsConstructor
public enum OpenAiErrorCode implements ErrorCode {

    OPENAI_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "OAI001", "OpenAI API 요청에 실패했습니다."),
    OPENAI_NO_CHOICES(HttpStatus.INTERNAL_SERVER_ERROR, "OAI002", "OpenAI로부터 응답 선택지가 없습니다."),
    OPENAI_UNEXPECTED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "OAI003", "OpenAI API 호출 중 예상치 못한 오류가 발생했습니다."),
    OPENAI_JSON_PARSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "OAI004", "OpenAI 응답 JSON 파싱에 실패했습니다."),
    OPENAI_SERVER_BUSY(HttpStatus.SERVICE_UNAVAILABLE, "OAI005", "현재 요청이 많아 처리가 지연되고 있습니다. 잠시 후 다시 시도해주세요."),
    SEMAPHORE_ACQUIRE_FAIL(HttpStatus.SERVICE_UNAVAILABLE, "OAI006", "세마포어 획득 실패"),

    // Distributed Lock
    DUPLICATE_REQUEST(HttpStatus.TOO_MANY_REQUESTS, "OAI007", "현재 요청을 처리 중입니다. 잠시 후 다시 시도해주세요."),
    LOCK_ACQUIRE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "OAI008", "락 획득에 실패했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
