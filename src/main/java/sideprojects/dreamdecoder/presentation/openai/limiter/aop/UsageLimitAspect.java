package sideprojects.dreamdecoder.presentation.openai.limiter.aop;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import sideprojects.dreamdecoder.presentation.openai.limiter.exception.UsageLimitErrorCode;
import sideprojects.dreamdecoder.presentation.openai.limiter.exception.UsageLimitExceededException;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class UsageLimitAspect {

    private final RedisTemplate<String, String> redisTemplate;

    @Around("@annotation(usageLimitCheck)")
    public Object execute(ProceedingJoinPoint joinPoint, UsageLimitCheck usageLimitCheck) throws Throwable {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        String today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        String key = "usage-limit:" + userId + ":" + today;

        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        Long count = ops.increment(key);



        if (count != null && count > usageLimitCheck.limit()) {
            throw new UsageLimitExceededException(UsageLimitErrorCode.USAGE_LIMIT_EXCEEDED);
        }

        log.info("============= Usage limit check: userId: {}, count: {}", userId, count);
        return joinPoint.proceed();
    }
}
