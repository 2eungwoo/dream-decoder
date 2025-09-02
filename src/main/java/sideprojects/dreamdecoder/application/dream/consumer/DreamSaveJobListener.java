package sideprojects.dreamdecoder.application.dream.consumer;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RStream;
import org.redisson.api.RedissonClient;
import org.redisson.api.StreamMessageId;
import org.redisson.api.stream.StreamCreateGroupArgs;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
@RequiredArgsConstructor
public class DreamSaveJobListener {

    private static final String STREAM_KEY = "dream:save:jobs";
    private static final String CONSUMER_GROUP = "dream-savers";
    private static final String CONSUMER_NAME = "consumer-" + java.util.UUID.randomUUID();

    private final RedissonClient redissonClient;
    private final DreamSaveJobPoller jobPoller;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @PostConstruct
    public void startListener() {
        setupStreamAndGroup();
        executorService.submit(this::runPollingLoop);
        log.info("DreamSaveJobListener 시작. 컨슈머 이름: {}", CONSUMER_NAME);
    }

    private void setupStreamAndGroup() {
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
    }

    private void runPollingLoop() {
        RStream<String, String> stream = redissonClient.getStream(STREAM_KEY);
        while (!Thread.currentThread().isInterrupted()) {
            jobPoller.poll(stream, CONSUMER_GROUP, CONSUMER_NAME);
        }
    }

    @PreDestroy
    public void stopListener() {
        executorService.shutdownNow();
        log.info("DreamSaveJobListener 종료.");
    }
}
