package sideprojects.dreamdecoder.infrastructure.external.openai.util;

import jakarta.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SemaphoreManager {

    private static final String SEMAPHORE_KEY = "semaphore:ai-chat";
    private static final long WAIT_TIME_SECONDS = 5L;

    private final RedissonClient redissonClient;

    @Value("${concurrency.permits}")
    private int permits;

    private RSemaphore semaphore;

    @PostConstruct
    public void init() {
        this.semaphore = redissonClient.getSemaphore(SEMAPHORE_KEY);
        this.semaphore.trySetPermits(permits);
    }

    public void acquire() throws InterruptedException {
        boolean acquired = semaphore.tryAcquire(WAIT_TIME_SECONDS, TimeUnit.SECONDS);

        if (!acquired) {
            throw new AiServerBusyException("현재 요청이 많아 처리가 지연되고 있습니다. 잠시 후 다시 시도해주세요.");
        }
    }

    public void release() {
        semaphore.release();
    }

}
