package sideprojects.dreamdecoder.infrastructure.external.openai.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RStream;
import org.redisson.api.RedissonClient;
import org.redisson.api.StreamMessageId;
import org.redisson.api.stream.StreamAddArgs;
import org.springframework.stereotype.Service;
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamType;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;
import sideprojects.dreamdecoder.infrastructure.external.openai.util.ConcurrencyManager;
import sideprojects.dreamdecoder.presentation.dream.dto.response.DreamInterpretationResponse;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DreamInterpretationService {

    private static final String STREAM_KEY = "dream:save:jobs";

    private final DreamSymbolExtractorService dreamSymbolExtractorService;
    private final DreamInterpretationGeneratorService dreamInterpretationGeneratorService;
    private final ConcurrencyManager concurrencyManager;
    private final RedissonClient redissonClient;
    private final ObjectMapper objectMapper;

    public DreamInterpretationResponse interpretDream(Long userId, String dreamContent, AiStyle style) {
        RLock lock = null;
        try {
            // 1. 동시성 자원 획득 (락, 세마포어)
            lock = acquireConcurrencyResources(userId);

            // 2. 핵심 비즈니스 로직 실행 및 스트림 메시지 발행
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

    // 핵심 로직 + 스트림 메시지 발행
    private DreamInterpretationResponse processDreamLogic(Long userId, String dreamContent, AiStyle style) {
        log.info("AI 서비스 요청 처리 시작 (유저 ID: {})", userId);

        AiStyle actualStyle = AiStyle.from(style);
        List<DreamType> extractedTypes = dreamSymbolExtractorService.extractSymbols(dreamContent);
        String interpretation = dreamInterpretationGeneratorService.generateInterpretation(actualStyle, extractedTypes, dreamContent);

        // Redis Stream에 저장할 메시지 생성
        try {
            RStream<String, String> stream = redissonClient.getStream(STREAM_KEY);
            StreamAddArgs<String, String> messageBody = StreamAddArgs.entries(
                    "userId", userId.toString(),
                    "dreamContent", dreamContent,
                    "interpretationResult", interpretation,
                    "aiStyle", actualStyle.name(),
                    "dreamTypes", objectMapper.writeValueAsString(extractedTypes)
            );
            StreamMessageId messageId = stream.add(messageBody);
            log.info("Redis Stream 메시지 발행 성공 (스트림 키: {}, 메시지 ID: {})", STREAM_KEY, messageId);
        } catch (Exception e) {
            log.error("Redis Stream 메시지 발행 중 오류 발생 (유저 ID: {})", userId, e);
            // TODO: 메시지 발행 실패 시 예외 처리 또는 알림 로직 추가
        }

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