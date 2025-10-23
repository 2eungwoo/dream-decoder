package sideprojects.dreamdecoder.infrastructure.external.openai.util;

import sideprojects.dreamdecoder.domain.dream.util.enums.DreamEmotion;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamType;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PromptGenerator {

    public static String generateSymbolExtractionSystemPrompt(List<String> allTypeNames) {
        String types = String.join(", ", allTypeNames);
        return "너는 꿈의 핵심 상징 추출 AI야. 사용자 꿈 내용에서 다음 키워드만 식별해: [" + types + "]. " +
                "응답은 반드시 JSON 문자열 배열이어야 해. 예: [\"PIG\", \"GOLD\"]. " +
                "다른 설명 없이 키워드만 포함해줘. 없으면 [\"NONE\"]을 반환해.";
    }

    public static String generateInterpretationSystemPrompt(AiStyle style, DreamEmotion dreamEmotion, List<DreamType> extractedTypes) {
        String symbols = extractedTypes.stream()
                .map(t -> String.format("'%s'(%s, %s)",
                        t.getDescription(),
                        t.getCategory().getDescription(),
                        t.getOutcome().getDescription()))
                .collect(Collectors.joining(", "));

        return String.format("너는 %s 스타일의 예지력 있는 꿈 해몽가야. 꿈의 핵심 상징은 %s, 느낀 감정은 '%s'이야. " +
                "이 상징과 감정을 종합적으로 해석하고, 일관된 이야기와 조언을 포함해서 400자 내외로 응답해줘.",
                style.getStyle(), symbols, dreamEmotion.getDescription());
    }
}
