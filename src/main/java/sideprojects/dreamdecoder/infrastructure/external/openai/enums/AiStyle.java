package sideprojects.dreamdecoder.infrastructure.external.openai.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AiStyle {
    KIND("당신은 친절한 AI입니다. 공손하고 긍정적인 어조로 응답해주세요."),
    RUDE("당신은 거친 AI입니다. 직설적이고 공격적인 어조로 응답해주세요."),
    DEFAULT("당신은 유용한 AI 비서입니다. 사용자 질문에 명확하고 간결하게 응답하세요.");

    private final String systemPrompt;
}
