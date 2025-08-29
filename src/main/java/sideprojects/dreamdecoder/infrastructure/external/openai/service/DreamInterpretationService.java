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
        RLock lock = concurrencyManager.acquireLock(userId);
        try {
            concurrencyManager.acquireSemaphore();
            try {
                log.info("AI 서비스 요청 처리 시작 (유저 ID: {})", userId);

                // 3. 핵심 비즈니스 로직
                AiStyle actualStyle = AiStyle.from(style);
                List<DreamType> extractedTypes = dreamSymbolExtractorService.extractSymbols(dreamContent);
                String interpretation = dreamInterpretationGeneratorService.generateInterpretation(actualStyle, extractedTypes, dreamContent);

                // 4. 이벤트 발행
                DreamInterpretationCompletedEvent event = new DreamInterpretationCompletedEvent(
                        userId, dreamContent, interpretation, actualStyle, extractedTypes
                );
                eventPublisher.publishEvent(event);
                log.info("AI 서비스 이벤트 발행 (유저 ID: {})", userId);

                // 5. 사용자에게 즉시 응답
                return DreamInterpretationResponse.of(interpretation, actualStyle, extractedTypes);

            } finally {
                // 6. 자원 해제 (세마포어)
                concurrencyManager.releaseSemaphore();
            }
        } finally {
            // 6. 자원 해제 (락)
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}

