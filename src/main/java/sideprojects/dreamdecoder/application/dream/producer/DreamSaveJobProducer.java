package sideprojects.dreamdecoder.application.dream.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RStream;
import org.redisson.api.RedissonClient;
import org.redisson.api.stream.StreamAddArgs;
import org.springframework.stereotype.Component;
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamEmotion;
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamType;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;
import sideprojects.dreamdecoder.application.dream.producer.DreamSaveJobCommand;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class DreamSaveJobProducer {

    private static final String STREAM_KEY = "dream:save:jobs";

    private final RedissonClient redissonClient;
    private final ObjectMapper objectMapper;

    public void publishJob(DreamSaveJobCommand command) {
        try {
            RStream<String, String> stream = redissonClient.getStream(STREAM_KEY);
            Map<String, String> messageBody = Map.of(
                    "userId", command.userId().toString(),
                    "dreamContent", command.dreamContent(),
                    "interpretationResult", command.interpretation(),
                    "dreamEmotion", command.dreamEmotion().name(),
                    "tags", command.tags(),
                    "aiStyle", command.style().name(),
                    "dreamTypes", objectMapper.writeValueAsString(command.types())
            );

            stream.add(StreamAddArgs.entries(messageBody));
            log.info("Redis Stream 메시지 발행 성공 (스트림 키: {})", STREAM_KEY);
        } catch (Exception e) {
            log.error("Redis Stream 메시지 발행 중 오류 발생 (유저 ID: {})", command.userId(), e);
            // TODO: 메시지 발행 실패에 대한 강력한 예외 처리 또는 알림 로직 추가 예정
        }
    }
}
