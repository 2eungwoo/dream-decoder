package sideprojects.dreamdecoder.application.web.dream.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RStream;
import org.redisson.api.RedissonClient;
import org.redisson.api.stream.StreamAddArgs;
import org.springframework.stereotype.Component;
import sideprojects.dreamdecoder.global.properties.RedisStreamProperties;

@Slf4j
@Component
@RequiredArgsConstructor
public class DreamSaveJobProducer {

    private final RedissonClient redissonClient;
    private final RedisStreamProperties redisStreamProperties;
    private final ObjectMapper objectMapper;

    public void publishJob(DreamSaveJobCommand command) {
        String streamKey = redisStreamProperties.getKey();
        RStream<String, String> stream = redissonClient.getStream(streamKey);

        try {
            String commandJson = objectMapper.writeValueAsString(command);
            Map<String, String> messageBody = Map.of("job", commandJson);

            stream.add(StreamAddArgs.entries(messageBody));
            log.info("Redis Stream 메시지 발행 성공 (스트림 키: {})", streamKey);
        } catch (JsonProcessingException e) {
            log.error("DreamSaveJobCommand 직렬화 실패", e);
            // todo: 직렬화 실패 시 에러 처리 정책 필요 (예: 예외 전파)
        }
    }
}
