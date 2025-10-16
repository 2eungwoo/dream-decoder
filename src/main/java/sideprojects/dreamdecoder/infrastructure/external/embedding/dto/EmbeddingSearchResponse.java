package sideprojects.dreamdecoder.infrastructure.external.embedding.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EmbeddingSearchResponse {

    @JsonProperty("dream_id")
    private Long dreamId;

    private Double score;

    public boolean hasResult() {
        return dreamId != null && score != null;
    }
}