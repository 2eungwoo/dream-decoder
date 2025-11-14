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
        return String.format(
                "You are an AI expert at extracting key symbols from dream descriptions. " +
                "Your task is to identify and extract specific keywords from the user's dream content. " +
                "You can ONLY extract from the following list of keywords: [%s]. " +
                "Your response MUST be a JSON string array. For example: [\"PIG\", \"GOLD\"]. " +
                "Do not include any explanations or any other text. " +
                "If no keywords are found, you MUST return [\"NONE\"].",
                types);
    }

    public static String generateInterpretationSystemPrompt(AiStyle style, DreamEmotion dreamEmotion, List<DreamType> extractedTypes) {
        String symbols = extractedTypes.stream()
                .map(t -> String.format("- %s: %s (Category: %s, Outcome: %s)",
                        t.name(),
                        t.getDescription(),
                        t.getCategory().getDescription(),
                        t.getOutcome().getDescription()))
                .collect(Collectors.joining("\n"));

        return String.format(
                "You are an insightful dream interpreter with a %s style. " +
                "Your mission is to interpret a dream based ONLY on the 'Interpretation Data' provided below. Do not use any other knowledge. " +
                "--- Interpretation Data ---\n" +
                "Emotion: %s\n" +
                "Key Symbols and Meanings:\n%s\n" +
                "--- End of Data ---\n" +
                "Based ONLY on the data above, provide a comprehensive interpretation of the user's dream. " +
                "Combine the symbols and emotion into a coherent story and give advice. " +
                "Your response MUST be in Korean and be approximately 400 characters long.",
                style.getStyle(), dreamEmotion.getDescription(), symbols);
    }
}
