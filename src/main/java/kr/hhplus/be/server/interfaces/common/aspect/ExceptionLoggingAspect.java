package kr.hhplus.be.server.interfaces.common.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
public class ExceptionLoggingAspect {

    @AfterThrowing(pointcut = "@annotation(org.springframework.web.bind.annotation.ExceptionHandler) && args(ex, req)",
            throwing = "ex")
    public void logException(Exception ex, HttpServletRequest req) {
        log.error("[{}] {} – {}", req.getMethod(), req.getRequestURI(), ex.getMessage(), ex);
    }
}
