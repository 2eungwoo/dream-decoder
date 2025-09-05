package sideprojects.dreamdecoder.application.dream.consumer;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RStream;
import org.redisson.api.StreamMessageId;
import org.redisson.api.stream.StreamReadGroupArgs;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class DreamSaveJobPoller {

    private final DreamSaveJobProcessor jobProcessor;

    public void poll(RStream<String, String> stream, String groupName, String consumerName) {
        try {
            StreamReadGroupArgs args = StreamReadGroupArgs.neverDelivered()
                    .count(1)
                    .timeout(Duration.ofSeconds(5));

            Map<StreamMessageId, Map<String, String>> messages = stream.readGroup(
                    groupName,
                    consumerName,
                    args
            );

            if (messages == null || messages.isEmpty()) {
                return;
            }

            for (Map.Entry<StreamMessageId, Map<String, String>> entry : messages.entrySet()) {
                log.info("메시지 수신: {}", entry.getKey());
                jobProcessor.process(stream, groupName, entry.getKey(), entry.getValue());
            }
        } catch (Exception e) {
            log.error("Redis Stream 읽기 중 오류 발생", e);
            sleepForRecovery();
        }
    }

    private void sleepForRecovery() {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
}
