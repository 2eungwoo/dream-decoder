package sideprojects.dreamdecoder.infrastructure.external.openai.util;

import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamType;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PromptGenerator {

    public static String generateSystemPrompt(AiStyle style) {
        return style.getSystemPrompt();
    }

    public static String generateSymbolExtractionSystemPrompt(List<String> allTypeNames) {
        String types = String.join(", ", allTypeNames);
        return "당신은 꿈 내용을 분석하고 핵심 상징을 추출하는 AI입니다. " + 
                "사용자의 꿈 이야기에서 다음 키워드 목록에 해당하는 모든 관련 상징을 식별하세요: [" + types + "]. " + 
                "당신의 응답은 반드시 문자열 JSON 배열이어야 하며, 각 문자열은 목록에서 일치하는 키워드 중 하나여야 합니다. " + 
                "예를 들어, 'PIG'와 'GOLD'를 식별했다면, 응답은 정확히 [\"PIG\", \"GOLD\"]여야 합니다. " + 
                "상징을 찾지 못했다면 빈 배열 []을 반환하세요. 어떠한 설명이나 서문도 추가하지 마세요.";
    }

    public static String generateInterpretationSystemPrompt(AiStyle style, List<DreamType> extractedTypes) {
        String symbols = extractedTypes.stream()
                .map(t -> String.format("'%s'(%s, %s)",
                        t.getDescription(),
                        t.getCategory().getDescription(),
                        t.getOutcome().getDescription()))
                .collect(Collectors.joining(", "));

        return String.format("당신은 자비롭고 현명한 꿈 해몽가입니다. 당신의 성격은 '%s'이어야 합니다. " +
                "사용자의 꿈에는 다음과 같은 핵심 상징들이 포함되어 있습니다: %s. " + 
                "이 상징들을 바탕으로 사용자의 꿈에 대한 포괄적인 해몽을 제공하세요. " +
                "상징들의 의미를 결합하여 일관성 있는 이야기를 전달하고, 해당되는 경우 조언을 제공하세요. " + 
                "사용자에게 직접적으로 말을 걸고 자연스럽고 대화적인 어조로 작성하세요.", style.getSystemPrompt(), symbols);
    }
}
