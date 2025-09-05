package sideprojects.dreamdecoder.infrastructure.external.openai.util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import sideprojects.dreamdecoder.global.config.ConcurrencyProperties;
import sideprojects.dreamdecoder.infrastructure.external.openai.util.exception.OpenAiApiException;
import sideprojects.dreamdecoder.infrastructure.external.openai.util.exception.OpenAiErrorCode;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class SemaphoreManager {

    private final RedissonClient redissonClient;
    private final ConcurrencyProperties concurrencyProperties;

    private RSemaphore semaphore;

    @PostConstruct
    public void init() {
        ConcurrencyProperties.Semaphore semaphoreProperties = concurrencyProperties.getSemaphore();
        this.semaphore = redissonClient.getSemaphore(semaphoreProperties.getKey());
        this.semaphore.trySetPermits(concurrencyProperties.getPermits());
    }

    public void acquireSemaphore() {
        try {
            long waitTime = concurrencyProperties.getSemaphore().getWaitTimeSeconds();
            boolean acquired = semaphore.tryAcquire(1, waitTime, TimeUnit.SECONDS);
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