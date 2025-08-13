package sideprojects.dreamdecoder.infrastructure.external.openai.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Getter
@RequiredArgsConstructor
public enum AiStyle {
    KIND("당신은 친절한 AI입니다. 공손하고 상냥한 어조로 응답해주세요."),
    PROPHET("당신은 고대의 예언자입니다. “…될지어다”, 같은 예언적 문장으로 처음부터 끝까지 응답하세요"),
    DEFAULT("당신은 유용한 AI 비서입니다. 사용자 질문에 명확하고 간결하게 응답하세요.");

    private final String style;

    public static AiStyle from(AiStyle style) {
        return (style != null) ? style : DEFAULT;
    }
}
