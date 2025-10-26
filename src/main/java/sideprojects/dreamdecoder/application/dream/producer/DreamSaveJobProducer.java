package sideprojects.dreamdecoder.application.dream.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RStream;
import org.redisson.api.RedissonClient;
import org.redisson.api.stream.StreamAddArgs;
import org.springframework.stereotype.Component;
import sideprojects.dreamdecoder.global.properties.RedisStreamProperties;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class DreamSaveJobProducer {

    private final RedissonClient redissonClient;
    private final RedisStreamProperties redisStreamProperties;

    public void publishJob(DreamSaveJobCommand command) {
        String streamKey = redisStreamProperties.getKey();
        RStream<String, String> stream = redissonClient.getStream(streamKey);

        // interpretation, types는 비동기 처리 이후 생성 -> 초기 발행 시 미포함
        Map<String, String> messageBody = new HashMap<>();
        messageBody.put("userId", command.userId().toString());
        messageBody.put("dreamContent", command.dreamContent());
        messageBody.put("dreamEmotion", command.dreamEmotion().name());
        messageBody.put("tags", command.tags());
        messageBody.put("style", command.style().name());

        stream.add(StreamAddArgs.entries(messageBody));
        log.info("Redis Stream 메시지 발행 성공 (스트림 키: {})", streamKey);
    }
}
