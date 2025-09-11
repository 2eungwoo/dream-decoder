package sideprojects.dreamdecoder.global.aop;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import sideprojects.dreamdecoder.infrastructure.external.openai.util.SemaphoreManager;

@Aspect
@Component
@RequiredArgsConstructor
public class SemaphoreAspect {

    private final SemaphoreManager semaphoreManager;

    @Around("@annotation(sideprojects.dreamdecoder.global.aop.UseSemaphore)")
    public Object manageSemaphore(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            semaphoreManager.acquireSemaphore();
            return joinPoint.proceed();
        } finally {
            semaphoreManager.releaseSemaphore();
        }
    }
}
