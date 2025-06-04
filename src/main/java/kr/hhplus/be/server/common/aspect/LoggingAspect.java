package kr.hhplus.be.server.common.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("execution(* kr.hhplus.be.server.application..*(..)) || " +
            "execution(* kr.hhplus.be.server.domain..*(..))")
    public Object logExecution(ProceedingJoinPoint joinPoint) throws Throwable {

        logger.info("[START] {}", joinPoint.getSignature());
        try {
            Object result = joinPoint.proceed();
            logger.info("[END] {}", joinPoint.getSignature());
            return result;
        } catch (Throwable ex) {
            logger.error("[EXCEPTION] {}", joinPoint.getSignature(), ex);
            throw ex;
        }
    }

}
