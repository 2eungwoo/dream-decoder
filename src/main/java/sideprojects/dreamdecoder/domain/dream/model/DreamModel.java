package sideprojects.dreamdecoder.domain.dream.model;

import lombok.Builder;
import lombok.Getter;
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamType;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;

import java.time.LocalDateTime;
import java.util.List;
import sideprojects.dreamdecoder.presentation.dream.dto.request.SaveDreamRequest;

@Getter
public class DreamModel {
    private final Long id;
    private final Long userId;
    private final String dreamContent;
    private final String interpretationResult;
    private final AiStyle aiStyle;
    private final List<DreamType> dreamTypes;
    private final LocalDateTime interpretedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    private DreamModel(Long id, Long userId, String dreamContent, String interpretationResult, AiStyle aiStyle, List<DreamType> dreamTypes, LocalDateTime interpretedAt, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.dreamContent = dreamContent;
        this.interpretationResult = interpretationResult;
        this.aiStyle = aiStyle;
        this.dreamTypes = dreamTypes;
        this.interpretedAt = interpretedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static DreamModel createNewDream(SaveDreamRequest request) {
        return DreamModel.builder()
                .userId(request.userId())
                .dreamContent(request.dreamContent())
                .interpretationResult(request.interpretationResult())
                .aiStyle(request.aiStyle())
                .dreamTypes(request.dreamTypes())
                .interpretedAt(LocalDateTime.now())
                .build();
    }
}
