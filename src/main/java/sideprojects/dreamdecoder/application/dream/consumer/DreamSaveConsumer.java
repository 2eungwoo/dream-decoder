package sideprojects.dreamdecoder.application.dream.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RStream;
import org.redisson.api.RedissonClient;
import org.redisson.api.StreamMessageId;
import org.redisson.api.stream.StreamCreateGroupArgs;
import org.redisson.api.stream.StreamReadGroupArgs;
import org.springframework.stereotype.Component;
import sideprojects.dreamdecoder.application.dream.usecase.save.SaveDreamUseCase;
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamType;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;
import sideprojects.dreamdecoder.presentation.dream.dto.request.SaveDreamRequest;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class DreamSaveConsumer {

    private static final String STREAM_KEY = "dream:save:jobs";
    private static final String CONSUMER_GROUP = "dream-savers";
    private static final String CONSUMER_NAME = "consumer-" + java.util.UUID.randomUUID();

    private final RedissonClient redissonClient;
    private final SaveDreamUseCase saveDreamUseCase;
    private final ObjectMapper objectMapper;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @PostConstruct
    public void startConsumer() {
        RStream<String, String> stream = redissonClient.getStream(STREAM_KEY);

        try {
            stream.createGroup(StreamCreateGroupArgs.name(CONSUMER_GROUP).id(StreamMessageId.ALL).makeStream());
            log.info("Redis Stream 컨슈머 그룹 생성 완료. 그룹: {}, 스트림 키: {}", CONSUMER_GROUP, STREAM_KEY);
        } catch (org.redisson.client.RedisException e) {
            if (e.getMessage().contains("BUSYGROUP")) {
                log.info("컨슈머 그룹이 이미 존재합니다: {}", CONSUMER_GROUP);
            } else {
                throw e;
            }
        }

        executorService.submit(this::consumeMessages);
        log.info("Redis Stream 컨슈머 시작. 컨슈머 이름: {}", CONSUMER_NAME);
    }

    private void consumeMessages() {
        RStream<String, String> stream = redissonClient.getStream(STREAM_KEY);
        while (!Thread.currentThread().isInterrupted()) {
            try {
                StreamReadGroupArgs args = StreamReadGroupArgs.neverDelivered()
                        .count(1)
                        .timeout(Duration.ofSeconds(5));

                Map<StreamMessageId, Map<String, String>> messages = stream.readGroup(
                        CONSUMER_GROUP,
                        CONSUMER_NAME,
                        args
                );

                if (messages == null || messages.isEmpty()) {
                    continue;
                }

                for (Map.Entry<StreamMessageId, Map<String, String>> entry : messages.entrySet()) {
                    StreamMessageId messageId = entry.getKey();
                    Map<String, String> messageBody = entry.getValue();
                    log.info("메시지 수신: {}", messageId);

                    try {
                        SaveDreamRequest request = buildRequestFromMessage(messageBody);
                        saveDreamUseCase.save(request);
                        stream.ack(CONSUMER_GROUP, messageId);
                        log.info("꿈 해석 결과 DB 저장 및 ACK 성공 (메시지 ID: {})", messageId);
                    } catch (Exception e) {
                        log.error("메시지 처리 중 오류 발생 (메시지 ID: {}), ACK하지 않음. 나중에 재처리됩니다.", messageId, e);
                    }
                }
            } catch (Exception e) {
                log.error("Redis Stream 읽기 중 오류 발생", e);
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private SaveDreamRequest buildRequestFromMessage(Map<String, String> message) throws JsonProcessingException {
        List<DreamType> dreamTypes = objectMapper.readValue(
                message.get("dreamTypes"),
                new TypeReference<>() {}
        );

        return SaveDreamRequest.builder()
                .userId(Long.parseLong(message.get("userId")))
                .dreamContent(message.get("dreamContent"))
                .interpretationResult(message.get("interpretationResult"))
                .aiStyle(AiStyle.valueOf(message.get("aiStyle")))
                .dreamTypes(dreamTypes)
                .build();
    }

    @PreDestroy
    public void stopConsumer() {
        executorService.shutdownNow();
        log.info("Redis Stream 컨슈머 종료.");
    }
}