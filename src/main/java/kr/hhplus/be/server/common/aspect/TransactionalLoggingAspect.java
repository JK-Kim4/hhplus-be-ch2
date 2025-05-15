package kr.hhplus.be.server.common.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;



@Aspect
@Component
public class TransactionalLoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(TransactionalLoggingAspect.class);

    // @Transactional 애노테이션이 붙은 메서드에 대해 Pointcut 지정
    @Pointcut("@annotation(transactional) || @within(transactional)")
    public void transactionalMethod(Transactional transactional) {}

    // 메서드 진입 시 로그
    @Before("transactionalMethod(transactional)")
    public void beforeTransactionMethod(JoinPoint joinPoint, Transactional transactional) {
        logger.info("@Transactional 진입 [Thread name: {}]",Thread.currentThread().getName());
    }

    // 정상 종료 시 로그
    @AfterReturning("transactionalMethod(transactional)")
    public void afterCommit(JoinPoint joinPoint, Transactional transactional) {
        logger.info("@Transactional 정상 종료 [Thread name: {}]",Thread.currentThread().getName());
    }

    // 예외 발생 시 로그
    @AfterThrowing("transactionalMethod(transactional)")
    public void afterRollback(JoinPoint joinPoint, Transactional transactional) {
        logger.info("@Transactional 예외 발생 [Thread name: {}]",Thread.currentThread().getName());
    }
}
