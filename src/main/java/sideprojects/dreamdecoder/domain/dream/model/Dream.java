package sideprojects.dreamdecoder.domain.dream.model;

import lombok.Builder;
import lombok.Getter;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;

import java.time.LocalDateTime;

@Getter
public class Dream {
    private Long id;
    private Long userId;
    private String dreamContent;
    private String interpretationResult;
    private AiStyle aiStyle;
    private LocalDateTime interpretedAt;

    @Builder
    private Dream(Long id, Long userId, String dreamContent, String interpretationResult, AiStyle aiStyle, LocalDateTime interpretedAt) {
        this.id = id;
        this.userId = userId;
        this.dreamContent = dreamContent;
        this.interpretationResult = interpretationResult;
        this.aiStyle = aiStyle;
        this.interpretedAt = interpretedAt;
    }

    public static Dream createNewDream(Long userId, String dreamContent, String interpretationResult, AiStyle aiStyle) {
        return Dream.builder()
                .userId(userId)
                .dreamContent(dreamContent)
                .interpretationResult(interpretationResult)
                .aiStyle(aiStyle)
                .interpretedAt(LocalDateTime.now())
                .build();
    }
}
