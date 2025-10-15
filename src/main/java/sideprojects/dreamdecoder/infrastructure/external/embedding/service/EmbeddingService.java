package sideprojects.dreamdecoder.infrastructure.external.embedding.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import sideprojects.dreamdecoder.global.config.WebClientConfig;
import sideprojects.dreamdecoder.infrastructure.external.embedding.dto.EmbeddingResponse;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmbeddingService {

    private final WebClientConfig webClient;

    @Value("${embedding.server.url}")
    private String embeddingServerUrl;

    public Mono<EmbeddingResponse> createEmbedding(String text) {
        String endpoint = embeddingServerUrl + "/encode";
        Map<String, String> requestBody = Collections.singletonMap("text", text);

        return webClient.post()
                .uri(endpoint)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(EmbeddingResponse.class);
    }
}
