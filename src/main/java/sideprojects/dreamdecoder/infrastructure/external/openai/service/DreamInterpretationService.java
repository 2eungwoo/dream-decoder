package sideprojects.dreamdecoder.infrastructure.external.openai.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.stereotype.Service;
import sideprojects.dreamdecoder.application.dream.producer.DreamSaveJobProducer;
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamType;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;
import sideprojects.dreamdecoder.infrastructure.external.openai.util.ConcurrencyManager;
import sideprojects.dreamdecoder.presentation.dream.dto.response.DreamInterpretationResponse;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DreamInterpretationService {

    private final DreamSymbolExtractorService dreamSymbolExtractorService;
    private final DreamInterpretationGeneratorService dreamInterpretationGeneratorService;
    private final ConcurrencyManager concurrencyManager;
    private final DreamSaveJobProducer dreamSaveJobProducer;

    public DreamInterpretationResponse interpretDream(Long userId, String dreamContent, AiStyle style) {
        RLock lock = null;
        try {
            lock = acquireConcurrencyResources(userId);
            return processDreamLogicAndPublishJob(userId, dreamContent, style);
        } finally {
            releaseConcurrencyResources(lock);
        }
    }

    private RLock acquireConcurrencyResources(Long userId) {
        RLock lock = concurrencyManager.acquireLock(userId);
        concurrencyManager.acquireSemaphore();
        return lock;
    }

    private DreamInterpretationResponse processDreamLogicAndPublishJob(Long userId, String dreamContent, AiStyle style) {
        log.info("AI 서비스 요청 처리 시작 (유저 ID: {})", userId);

        AiStyle actualStyle = AiStyle.from(style);
        List<DreamType> extractedTypes = dreamSymbolExtractorService.extractSymbols(dreamContent);
        String interpretation = dreamInterpretationGeneratorService.generateInterpretation(actualStyle, extractedTypes, dreamContent);

        dreamSaveJobProducer.publishJob(userId, dreamContent, interpretation, actualStyle, extractedTypes);

        return DreamInterpretationResponse.of(interpretation, actualStyle, extractedTypes);
    }

    private void releaseConcurrencyResources(RLock lock) {
        try {
            concurrencyManager.releaseSemaphore();
        } finally {
            if (lock != null && lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}