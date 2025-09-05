package sideprojects.dreamdecoder.infrastructure.external.openai.util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sideprojects.dreamdecoder.infrastructure.external.openai.util.exception.OpenAiApiException;
import sideprojects.dreamdecoder.infrastructure.external.openai.util.exception.OpenAiErrorCode;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class SemaphoreManager {

    private static final String SEMAPHORE_KEY = "semaphore:ai-chat";
    private static final long SEMAPHORE_WAIT_TIME_SECONDS = 5L;

    private final RedissonClient redissonClient;

    @Value("${concurrency.permits}")
    private int permits;

    private RSemaphore semaphore;

    @PostConstruct
    public void init() {
        this.semaphore = redissonClient.getSemaphore(SEMAPHORE_KEY);
        this.semaphore.trySetPermits(permits);
    }

    public void acquireSemaphore() {
        try {
            boolean acquired = semaphore.tryAcquire(1, SEMAPHORE_WAIT_TIME_SECONDS, TimeUnit.SECONDS);
            if (!acquired) {
                throw new OpenAiApiException(OpenAiErrorCode.SEMAPHORE_ACQUIRE_FAIL);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new OpenAiApiException(OpenAiErrorCode.SEMAPHORE_ACQUIRE_FAIL, e);
        }
    }

    public void releaseSemaphore() {
        semaphore.release();
    }
}
