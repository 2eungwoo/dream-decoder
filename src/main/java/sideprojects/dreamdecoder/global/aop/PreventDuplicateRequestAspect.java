package sideprojects.dreamdecoder.global.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import sideprojects.dreamdecoder.global.properties.DuplicateRequestLockProperties;
import sideprojects.dreamdecoder.infrastructure.external.openai.util.exception.OpenAiApiException;
import sideprojects.dreamdecoder.infrastructure.external.openai.util.exception.OpenAiErrorCode;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.Arrays;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class PreventDuplicateRequestAspect {

    private final RedissonClient redissonClient;
    private final DuplicateRequestLockProperties lockProperties;

    @Around("@annotation(preventDuplicateRequest)")
    public Object preventDuplicateRequest(ProceedingJoinPoint joinPoint, PreventDuplicateRequest preventDuplicateRequest) throws Throwable {
        log.info("===== [AOP] PreventDuplicateRequestAspect 시작 =====");
        String dynamicKey = createDynamicKey(joinPoint, preventDuplicateRequest.key());
        String lockKey = lockProperties.getKeyPrefix() + dynamicKey;
        log.info("[AOP] 생성된 중복 방지 키: {}", lockKey);

        Duration ttl = lockProperties.getTtl();
        RBucket<String> bucket = redissonClient.getBucket(lockKey);

        // SET if Not Exists, with TTL
        log.info("[AOP] 중복 방지 키 설정 시도... (TTL: {}초)", ttl.toSeconds());
        boolean set = bucket.trySet("locked", ttl.toSeconds(), TimeUnit.SECONDS);
        log.info("[AOP] 키 설정 결과: {}", set);

        if (!set) {
            log.warn("[AOP] 키가 이미 존재함. 중복 요청으로 판단. Key: {}", lockKey);
            throw new OpenAiApiException(OpenAiErrorCode.DUPLICATE_REQUEST);
        }

        log.info("[AOP] 키 설정 성공. 비즈니스 로직 실행. Key: {}", lockKey);
        return joinPoint.proceed();
    }

    private String createDynamicKey(ProceedingJoinPoint joinPoint, String keyExpression) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String[] parameterNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        log.debug("[AOP-DEBUG] SpEL: {}", keyExpression);
        log.debug("[AOP-DEBUG] 메소드 파라미터 이름s: {}", Arrays.toString(parameterNames));
        log.debug("[AOP-DEBUG] 메소드 인자: {}", Arrays.toString(args));

        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();

        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }

        String resultKey = String.valueOf(parser.parseExpression(keyExpression).getValue(context));
        log.debug("[AOP-DEBUG] Key: {}", resultKey);
        return resultKey;
    }
}
