package sideprojects.dreamdecoder.infrastructure.external.embedding.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import sideprojects.dreamdecoder.infrastructure.external.embedding.dto.EmbeddingAddRequest;
import sideprojects.dreamdecoder.infrastructure.external.embedding.dto.EmbeddingSearchRequest;
import sideprojects.dreamdecoder.infrastructure.external.embedding.dto.EmbeddingSearchResponse;

@Service
@RequiredArgsConstructor
public class EmbeddingService {

    private final WebClient webClient;

    @Value("${embedding.server.url}")
    private String embeddingServerUrl;

    public Mono<EmbeddingSearchResponse> searchSimilarDream(String text) {
        String endpoint = embeddingServerUrl + "/search";
        EmbeddingSearchRequest requestBody = new EmbeddingSearchRequest(text);

        return webClient.post()
                .uri(endpoint)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(EmbeddingSearchResponse.class);
    }

    public Mono<Void> addDreamVector(long dreamId, String text) {
        String endpoint = embeddingServerUrl + "/add";
        EmbeddingAddRequest requestBody = new EmbeddingAddRequest(dreamId, text);

        return webClient.post()
                .uri(endpoint)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Void.class);
    }
}