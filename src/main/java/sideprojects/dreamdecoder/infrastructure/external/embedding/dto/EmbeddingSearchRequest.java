package sideprojects.dreamdecoder.infrastructure.external.embedding.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmbeddingSearchRequest {
    private String text;
}
