package sideprojects.dreamdecoder.application.dream.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RStream;
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

    public void process(RStream<String, String> stream, String groupName, StreamMessageId messageId,
                        Map<String, String> messageBody) {
        try {
            // 1. 메시지 역직렬화
            String jobJson = messageBody.get("job");
            DreamSaveJobCommand command = objectMapper.readValue(jobJson, DreamSaveJobCommand.class);

            // 2. 서비스 레이어에 전달할 페이로드 생성
            DreamJobPayload payload = new DreamJobPayload(
                command.userId(),
                command.dreamContent(),
                command.dreamEmotion(),
                command.style(),
                command.tags()
            );

            // 3. processor에 비즈니스 로직 위임
            dreamProcessingService.interpretAndSave(payload);

            // 4. 성공 시 ACK
            stream.ack(groupName, messageId);
            log.info("메시지 처리 및 ACK 성공 (메시지 ID: {})", messageId);

        } catch (JsonProcessingException e) {
            log.error("메시지 역직렬화 실패 (메시지 ID: {}), 메시지를 버립니다.", messageId, e);
            // 역직렬화 실패는 재시도해도 성공할 가능성이 없으므로, ACK 처리하여 메시지 큐에서 제거
            stream.ack(groupName, messageId);
        } catch (Exception e) {
            log.error("메시지 처리 중 오류 발생 (메시지 ID: {}), ACK하지 않음, 나중에 재처리됨", messageId, e);
        }
    }
}
