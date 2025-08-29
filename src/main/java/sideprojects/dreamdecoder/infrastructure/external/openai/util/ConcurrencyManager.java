package sideprojects.dreamdecoder.infrastructure.external.openai.util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sideprojects.dreamdecoder.global.shared.exception.CustomException;
import sideprojects.dreamdecoder.infrastructure.external.openai.util.exception.OpenAiApiException;
import sideprojects.dreamdecoder.infrastructure.external.openai.util.exception.OpenAiErrorCode;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class ConcurrencyManager {

    private static final String LOCK_KEY_PREFIX = "lock:ai-chat:user:";
    private static final String SEMAPHORE_KEY = "semaphore:ai-chat";
    private static final long LOCK_WAIT_TIME_SECONDS = 0L; // 락은 즉시 실패 응답을 위해 대기하지 않음
    private static final long LOCK_LEASE_TIME_SECONDS = 30L; // 락 자동 해제 시간
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

    public RLock acquireLock(Long userId) {
        RLock lock = redissonClient.getLock(LOCK_KEY_PREFIX + userId);
        try {
            boolean acquired = lock.tryLock(LOCK_WAIT_TIME_SECONDS, LOCK_LEASE_TIME_SECONDS, TimeUnit.SECONDS);
            if (!acquired) {
                throw new OpenAiApiException(OpenAiErrorCode.DUPLICATE_REQUEST);
            }
            return lock;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new OpenAiApiException(OpenAiErrorCode.LOCK_ACQUIRE_FAIL, e);
        }
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