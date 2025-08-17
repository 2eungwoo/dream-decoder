package sideprojects.dreamdecoder.infrastructure.external.openai.service;

import java.util.concurrent.ThreadLocalRandom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DummyService {

    public String interpretDream(Long userId, String dreamContent)
        throws InterruptedException {
        // 10s~30s 랜덤 지연시간
        long delay = ThreadLocalRandom.current().nextLong(10000, 30001);
        Thread.sleep(delay);

        String dummyContent = String.format(
            "이것은 더미 AI의 꿈 해몽 결과입니다. (유저 '%s', 입력내용: '%s', 처리 시간: %dms)",
            userId.toString(),
            dreamContent,
            delay
        );

        return dummyContent;
    }
}
