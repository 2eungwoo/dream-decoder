package sideprojects.dreamdecoder.application.web.dream.consumer;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RStream;
import org.redisson.api.RedissonClient;
import org.redisson.api.StreamMessageId;
import org.redisson.api.stream.StreamCreateGroupArgs;
import org.springframework.stereotype.Component;
import sideprojects.dreamdecoder.global.properties.RedisStreamProperties;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
@RequiredArgsConstructor
public class DreamSaveJobListener {

    private final RedissonClient redissonClient;
    private final DreamSaveJobPoller jobPoller;
    private final RedisStreamProperties redisStreamProperties;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private String consumerName;

    @PostConstruct
    public void startListener() {
        this.consumerName = redisStreamProperties.getConsumerPrefix() + UUID.randomUUID();
        setupStreamAndGroup();
        executorService.submit(this::runPollingLoop);
        log.info("DreamSaveJobListener 시작. 컨슈머 이름: {}", consumerName);
    }

    private void setupStreamAndGroup() {
        String streamKey = redisStreamProperties.getKey();
        String groupName = redisStreamProperties.getGroup();
        RStream<String, String> stream = redissonClient.getStream(streamKey);
        try {
            stream.createGroup(StreamCreateGroupArgs.name(groupName).id(new StreamMessageId(0, 0)).makeStream());
            log.info("Redis Stream 컨슈머 그룹 생성 완료. 그룹: {}, 스트림 키: {}", groupName, streamKey);
        } catch (org.redisson.client.RedisException e) {
            if (e.getMessage().contains("BUSYGROUP")) {
                log.info("컨슈머 그룹이 이미 존재합니다: {}", groupName);
            } else {
                throw e;
            }
        }
    }

    private void runPollingLoop() {
        String streamKey = redisStreamProperties.getKey();
        String groupName = redisStreamProperties.getGroup();
        RStream<String, String> stream = redissonClient.getStream(streamKey);
        while (!Thread.currentThread().isInterrupted()) {
            jobPoller.poll(stream, groupName, consumerName);
        }
    }

    @PreDestroy
    public void stopListener() {
        executorService.shutdownNow();
        log.info("DreamSaveJobListener 종료.");
    }
}
