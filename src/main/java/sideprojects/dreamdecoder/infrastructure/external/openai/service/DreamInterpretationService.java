package sideprojects.dreamdecoder.infrastructure.external.openai.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import sideprojects.dreamdecoder.application.dream.producer.DreamSaveJobProducer;
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamType;
import sideprojects.dreamdecoder.global.config.DuplicateRequestLockProperties;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;
import sideprojects.dreamdecoder.infrastructure.external.openai.util.SemaphoreManager;
import sideprojects.dreamdecoder.infrastructure.external.openai.util.exception.OpenAiApiException;
import sideprojects.dreamdecoder.infrastructure.external.openai.util.exception.OpenAiErrorCode;
import sideprojects.dreamdecoder.presentation.dream.dto.response.DreamInterpretationResponse;

import java.time.Duration;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DreamInterpretationService {

    private final DreamSymbolExtractorService dreamSymbolExtractorService;
    private final DreamInterpretationGeneratorService dreamInterpretationGeneratorService;
    private final SemaphoreManager semaphoreManager;
    private final DreamSaveJobProducer dreamSaveJobProducer;
    private final RedisTemplate<String, String> redisTemplate;
    private final DuplicateRequestLockProperties duplicateRequestLockProperties;

    public DreamInterpretationResponse interpretDream(Long userId, String dreamContent, AiStyle style) {
        checkDuplicateRequest(userId);
        try {
            semaphoreManager.acquireSemaphore();
            return processDreamLogicAndPublishJob(userId, dreamContent, style);
        } finally {
            semaphoreManager.releaseSemaphore();
        }
    }

    private void checkDuplicateRequest(Long userId) {
        String lockKey = duplicateRequestLockProperties.getKeyPrefix() + userId;
        Duration ttl = duplicateRequestLockProperties.getTtl();

        Boolean acquired = redisTemplate.opsForValue()
                .setIfAbsent(lockKey, "locked", ttl);

        if (Boolean.FALSE.equals(acquired)) {
            throw new OpenAiApiException(OpenAiErrorCode.DUPLICATE_REQUEST);
        }
    }

    private DreamInterpretationResponse processDreamLogicAndPublishJob(Long userId, String dreamContent, AiStyle style) {
        log.info("AI 서비스 요청 처리 시작 (유저 ID: {})", userId);

        AiStyle actualStyle = AiStyle.from(style);
        List<DreamType> extractedTypes = dreamSymbolExtractorService.extractSymbols(dreamContent);
        String interpretation = dreamInterpretationGeneratorService.generateInterpretation(actualStyle, extractedTypes, dreamContent);

        dreamSaveJobProducer.publishJob(userId, dreamContent, interpretation, actualStyle, extractedTypes);

        return DreamInterpretationResponse.of(interpretation, actualStyle, extractedTypes);
    }
}
