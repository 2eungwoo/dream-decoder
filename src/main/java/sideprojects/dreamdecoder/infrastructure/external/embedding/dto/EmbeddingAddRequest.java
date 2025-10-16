package sideprojects.dreamdecoder.infrastructure.external.embedding.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmbeddingAddRequest {

    @JsonProperty("dream_id")
    private long dreamId;
    private String text;
}
