package sideprojects.dreamdecoder.domain.dream.model;

import lombok.Builder;
import lombok.Getter;
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamType;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class DreamModel {
    private Long id;
    private Long userId;
    private String dreamContent;
    private String interpretationResult;
    private AiStyle aiStyle;
    private List<DreamType> dreamTypes;
    private LocalDateTime interpretedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    private DreamModel(Long id, Long userId, String dreamContent, String interpretationResult, AiStyle aiStyle, List<DreamType> dreamTypes, LocalDateTime interpretedAt) {
        this.id = id;
        this.userId = userId;
        this.dreamContent = dreamContent;
        this.interpretationResult = interpretationResult;
        this.aiStyle = aiStyle;
        this.dreamTypes = dreamTypes;
        this.interpretedAt = interpretedAt;
    }

    public static DreamModel createNewDream(Long userId, String dreamContent, String interpretationResult, AiStyle aiStyle, List<DreamType> dreamTypes) {
        return DreamModel.builder()
                .userId(userId)
                .dreamContent(dreamContent)
                .interpretationResult(interpretationResult)
                .aiStyle(aiStyle)
                .dreamTypes(dreamTypes)
                .interpretedAt(LocalDateTime.now())
                .build();
    }
}
