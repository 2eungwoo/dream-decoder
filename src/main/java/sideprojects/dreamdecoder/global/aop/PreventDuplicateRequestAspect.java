package sideprojects.dreamdecoder.global.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import sideprojects.dreamdecoder.global.config.DuplicateRequestLockProperties;
import sideprojects.dreamdecoder.infrastructure.external.openai.util.exception.OpenAiApiException;
import sideprojects.dreamdecoder.infrastructure.external.openai.util.exception.OpenAiErrorCode;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class PreventDuplicateRequestAspect {

    private final RedissonClient redissonClient;
    private final DuplicateRequestLockProperties lockProperties;

    @Around("@annotation(preventDuplicateRequest)")
    public Object preventDuplicateRequest(ProceedingJoinPoint joinPoint, PreventDuplicateRequest preventDuplicateRequest) throws Throwable {
        String dynamicKey = createDynamicKey(joinPoint, preventDuplicateRequest.key());
        String lockKey = lockProperties.getKeyPrefix() + dynamicKey;
        Duration ttl = lockProperties.getTtl();
        RLock lock = redissonClient.getLock(lockKey);

        boolean acquired = lock.tryLock(0, ttl.toSeconds(), TimeUnit.SECONDS);

        if (!acquired) {
            log.warn("중복 요청이 감지되었습니다. Key: {}", lockKey);
            throw new OpenAiApiException(OpenAiErrorCode.DUPLICATE_REQUEST);
        }

        log.info("락 획득 성공. Key: {}", lockKey);
        try {
            return joinPoint.proceed();
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
                log.info("락 해제 성공. Key: {}", lockKey);
            }
        }
    }

    private String createDynamicKey(ProceedingJoinPoint joinPoint, String keyExpression) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String[] parameterNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();

        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }

        return String.valueOf(parser.parseExpression(keyExpression).getValue(context));
    }
}
