package sideprojects.dreamdecoder.application.dream.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RStream;
import org.redisson.api.RedissonClient;
import org.redisson.api.StreamMessageId;
import org.springframework.stereotype.Component;
import sideprojects.dreamdecoder.application.dream.producer.DreamSaveJobCommand;
import sideprojects.dreamdecoder.application.dream.service.DreamProcessingService;
import sideprojects.dreamdecoder.application.dream.service.interpreter.DreamJobPayload;

@Slf4j
@Component
@RequiredArgsConstructor
public class DreamSaveJobProcessor {

    private final DreamProcessingService dreamProcessingService;
    private final ObjectMapper objectMapper;
    private final RedissonClient redissonClient;
    private static final String LOCK_KEY_PREFIX = "lock:consumer:";

    public void process(RStream<String, String> stream, String groupName, StreamMessageId messageId,
                        Map<String, String> messageBody) {
        try {
            // 1. 메시지 역직렬화
            String jobJson = messageBody.get("job");
            DreamSaveJobCommand command = objectMapper.readValue(jobJson, DreamSaveJobCommand.class);

            // 2. 분산락 처리 추가(key:컨트롤러에서 넘어온 idempotecyKey)
            // prefix는 나중에 yml로 빼든지 할 예정 todo:근데 상수코딩 없애자고 전부 yml같은 영역에 빼는게 좋은건지 고민 중
            String lockKey = LOCK_KEY_PREFIX + command.idempotencyKey();
            RLock lock = redissonClient.getLock(lockKey);

            // 락 획득 시도 (max 5s 대기, 획득 후 10m 유지, 시간은 일단 임의설정함)
            boolean acquired = lock.tryLock(5, 10, TimeUnit.MINUTES);

            if (!acquired) {
                log.warn("다른 컨슈머가 작업을 처리 중이므로 현재 메시지를 건너뜁니다. Lock Key: {}, Message ID: {}", lockKey, messageId);
                // 다른 컨슈머가 처리중이거나 완료 상태의 경우임
                // 현재 메시지는 ACK 처리 -> 큐에서 제거 처리
                stream.ack(groupName, messageId);
                return;
            }

            try {
                // 3. 서비스 레이어에 전달할 페이로드
                DreamJobPayload payload = new DreamJobPayload(
                    command.userId(),
                    command.dreamContent(),
                    command.dreamEmotion(),
                    command.style(),
                    command.tags()
                );

                // 4. processor에 비즈니스 로직 위임
                dreamProcessingService.interpretAndSave(payload);

                // 5. 다 되면 ACK
                stream.ack(groupName, messageId);
                log.info("메시지 처리 및 ACK 성공 (메시지 ID: {})", messageId);

            } finally {
                // 6. 락 해제
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }

        } catch (JsonProcessingException e) {
            log.error("메시지 역직렬화 실패 (메시지 ID: {}), 메시지를 버립니다.", messageId, e);
            // 역직렬화 실패 경우는 재시도해도 성공할 가능성이 없음
            // json프로세싱 예외 발생하면 ACK 처리해서 메시지 큐에서 제거 todo: 여기 최종 실패의 경우 이제 어떻게 해줘야할지 고민
            stream.ack(groupName, messageId);
        } catch (Exception e) {
            log.error("메시지 처리 중 오류 발생 (메시지 ID: {}), ACK하지 않음, 나중에 재처리됨", messageId, e);
        }
    }
}
