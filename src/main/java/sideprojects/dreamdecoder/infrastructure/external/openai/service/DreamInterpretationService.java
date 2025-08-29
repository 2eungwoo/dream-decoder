package sideprojects.dreamdecoder.infrastructure.external.openai.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import sideprojects.dreamdecoder.application.dream.event.DreamInterpretationCompletedEvent;
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
    private final ApplicationEventPublisher eventPublisher;

    public DreamInterpretationResponse interpretDream(Long userId, String dreamContent, AiStyle style) {
        RLock lock = null;
        try {
            // 1. 동시성 자원 획득 (락, 세마포어)
            lock = acquireConcurrencyResources(userId);

            // 2. 핵심 비즈니스 로직 실행 및 이벤트 발행
            return processDreamLogic(userId, dreamContent, style);

        } finally {
            // 3. 동시성 자원 해제 (세마포어 및 락)
            releaseConcurrencyResources(lock);
        }
    }

    // 동시성 자원 획득 (분산 락 + 분산 세마포어)
    private RLock acquireConcurrencyResources(Long userId) {
        RLock lock = concurrencyManager.acquireLock(userId);
        concurrencyManager.acquireSemaphore();
        return lock;
    }

    // 핵심 로직 + 이벤트 발행
    private DreamInterpretationResponse processDreamLogic(Long userId, String dreamContent, AiStyle style) {
        log.info("AI 서비스 요청 처리 시작 (유저 ID: {})", userId);

        AiStyle actualStyle = AiStyle.from(style);
        List<DreamType> extractedTypes = dreamSymbolExtractorService.extractSymbols(dreamContent);
        String interpretation = dreamInterpretationGeneratorService.generateInterpretation(actualStyle, extractedTypes, dreamContent);

        DreamInterpretationCompletedEvent event = new DreamInterpretationCompletedEvent(
                userId, dreamContent, interpretation, actualStyle, extractedTypes
        );
        eventPublisher.publishEvent(event);
        log.info("AI 서비스 이벤트 발행 (유저 ID: {})", userId);

        return DreamInterpretationResponse.of(interpretation, actualStyle, extractedTypes);
    }

    // 동시성 자원 해제 (분산 락 + 분산 세마포어)
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