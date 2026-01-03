package com.nexushub.NexusHub.Common.Aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
@Slf4j
class ExecutionTimeAspect {

    @Around("@annotation(com.nexushub.NexusHub.Common.Annotation.LogExecutionTime)") // 1단계에서 만든 어노테이션 경로
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Object proceed = joinPoint.proceed(); // 실제 메서드 실행

        stopWatch.stop();
        log.info("[Execution Time] {} - {}ms", joinPoint.getSignature().getName(), stopWatch.getTotalTimeMillis());

        return proceed;
    }
}
