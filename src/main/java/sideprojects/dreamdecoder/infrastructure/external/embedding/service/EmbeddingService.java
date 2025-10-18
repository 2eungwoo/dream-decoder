package sideprojects.dreamdecoder.infrastructure.external.embedding.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamEmotion;
import sideprojects.dreamdecoder.infrastructure.external.embedding.dto.EmbeddingAddRequest;
import sideprojects.dreamdecoder.infrastructure.external.embedding.dto.EmbeddingSearchRequest;
import sideprojects.dreamdecoder.infrastructure.external.embedding.dto.EmbeddingSearchResponse;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmbeddingService {

    private final RestTemplate restTemplate;

    @Value("${embedding.server.url}")
    private String embeddingServerUrl;

    public EmbeddingSearchResponse searchSimilarDream(String text, AiStyle style, DreamEmotion emotion) {
        String endpoint = embeddingServerUrl + "/search";
        EmbeddingSearchRequest requestBody = new EmbeddingSearchRequest(text, style.name(), emotion.name());

        try {
            return restTemplate.postForObject(endpoint, requestBody, EmbeddingSearchResponse.class);
        } catch (RestClientException e) {
            log.error("임베딩 서버 유사 꿈 검색 실패: {}", e.getMessage(), e);
            return null;
        }
    }

    @Async("taskExecutor")
    public void addDreamVector(long dreamId, String text, AiStyle style, DreamEmotion emotion) {
        String endpoint = embeddingServerUrl + "/add";
        EmbeddingAddRequest requestBody = new EmbeddingAddRequest(dreamId, text, style.name(), emotion.name());

        try {
            String response = restTemplate.postForObject(endpoint, requestBody, String.class);
            log.info("[L2 캐시 쓰기 성공] 응답: {}", response);
        } catch (RestClientException e) {
            log.error("[L2 캐시 쓰기 실패] 꿈 ID: {} 벡터 DB 추가 중 에러 발생: {}", dreamId, e.getMessage(), e);
        }
    }
}
