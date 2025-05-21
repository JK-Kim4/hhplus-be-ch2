package kr.hhplus.be.server.common.aspect;

import kr.hhplus.be.server.common.annotation.ExceptionRecordable;
import kr.hhplus.be.server.domain.history.exception.ExceptionHistory;
import kr.hhplus.be.server.domain.history.exception.ExceptionHistoryFactory;
import kr.hhplus.be.server.domain.history.exception.ExceptionHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Aspect
@Component
@Slf4j
public class ExceptionHistoryAspect {

    private final ExceptionHistoryService exceptionHistoryService;

    public ExceptionHistoryAspect(ExceptionHistoryService exceptionHistoryService) {
        this.exceptionHistoryService = exceptionHistoryService;
    }

    @AfterThrowing(pointcut = "@annotation(exceptionRecordable)")
    public void recordException(JoinPoint joinPoint, ExceptionRecordable exceptionRecordable) {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        boolean retryable = exceptionRecordable.retryable();

        ExceptionHistory history = ExceptionHistoryFactory.create(methodName, args, retryable);
        log.error("Exception occurred in method: {} with args: {}. Retryable: {}", methodName, args, retryable);

        //Redis 저장소에 오류 이력 저장
        exceptionHistoryService.saveExceptionHistory(LocalDate.now(), history);
    }



}
