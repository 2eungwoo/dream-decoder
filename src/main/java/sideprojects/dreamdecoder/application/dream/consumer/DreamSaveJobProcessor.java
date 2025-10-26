package sideprojects.dreamdecoder.application.dream.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RStream;
import org.redisson.api.StreamMessageId;
import org.springframework.stereotype.Component;
import sideprojects.dreamdecoder.application.dream.service.DreamProcessingService;
import sideprojects.dreamdecoder.application.dream.service.interpreter.DreamJobPayload;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class DreamSaveJobProcessor {

    private final DreamProcessingService dreamProcessingService;

    public void process(RStream<String, String> stream, String groupName, StreamMessageId messageId,
                        Map<String, String> messageBody) {
        try {
            // 1. 메시지 파싱
            DreamJobPayload payload = DreamJobPayload.fromMessage(messageBody);

            // 2. 오케스트레이터에게 모든 비즈니스 로직 위임
            dreamProcessingService.interpretAndSave(payload);

            // 3. 성공 시 ACK
            stream.ack(groupName, messageId);
            log.info("메시지 처리 및 ACK 성공 (메시지 ID: {})", messageId);

        } catch (Exception e) {
            log.error("메시지 처리 중 오류 발생 (메시지 ID: {}), ACK하지 않음, 나중에 재처리됨", messageId, e);
        }
    }
}
