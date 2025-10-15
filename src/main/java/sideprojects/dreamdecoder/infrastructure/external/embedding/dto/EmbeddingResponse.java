package sideprojects.dreamdecoder.infrastructure.external.embedding.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class EmbeddingResponse {

    @JsonProperty("vector")
    private List<Double> vector;
}
