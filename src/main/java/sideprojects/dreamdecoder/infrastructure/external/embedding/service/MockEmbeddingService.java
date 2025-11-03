package sideprojects.dreamdecoder.infrastructure.external.embedding.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamEmotion;
import sideprojects.dreamdecoder.infrastructure.external.embedding.dto.EmbeddingSearchResponse;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;

@Slf4j
@Service
@Profile("load-test")
@Primary
public class MockEmbeddingService extends EmbeddingService {

    // RestTemplate도 필요없음
    public MockEmbeddingService() {
        super(null);
    }

    @Override
    public EmbeddingSearchResponse searchSimilarDream(String dreamContent, AiStyle style, DreamEmotion dreamEmotion) {
        log.info("[LOAD TEST] MockEmbeddingService.searchSimilarDream is called. Returning cache miss.");
        // 항상 캐시 미스로 리턴 -> L2 벡터 캐시 스킵
        return null;
    }

    @Override
    public void addDreamVector(long dreamId, String dreamContent, AiStyle style, DreamEmotion dreamEmotion) {
        log.info("[LOAD TEST] MockEmbeddingService.addDreamVector is called. Doing nothing.");
        // 벡터를 추가하는 척만 하고 아무것도 안해버리기
    }
}
