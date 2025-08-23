package sideprojects.dreamdecoder.infrastructure.external.openai.util;

import jakarta.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sideprojects.dreamdecoder.infrastructure.external.openai.util.exception.AiServerBusyException;
import sideprojects.dreamdecoder.infrastructure.external.openai.util.exception.OpenAiErrorCode;

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
            throw new AiServerBusyException(OpenAiErrorCode.OPENAI_SERVER_BUSY);
        }
    }

    public void release() {
        semaphore.release();
    }

}
