package sideprojects.dreamdecoder.domain.dream.model;

import lombok.Builder;
import lombok.Getter;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;

import java.time.LocalDateTime;

@Getter
public class DreamModel {
    private Long id;
    private Long userId;
    private String dreamContent;
    private String interpretationResult;
    private AiStyle aiStyle;
    private LocalDateTime interpretedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    private DreamModel(Long id, Long userId, String dreamContent, String interpretationResult, AiStyle aiStyle, LocalDateTime interpretedAt) {
        this.id = id;
        this.userId = userId;
        this.dreamContent = dreamContent;
        this.interpretationResult = interpretationResult;
        this.aiStyle = aiStyle;
        this.interpretedAt = interpretedAt;
    }

    public static DreamModel createNewDream(Long userId, String dreamContent, String interpretationResult, AiStyle aiStyle) {
        return DreamModel.builder()
                .userId(userId)
                .dreamContent(dreamContent)
                .interpretationResult(interpretationResult)
                .aiStyle(aiStyle)
                .interpretedAt(LocalDateTime.now())
                .build();
    }
}
