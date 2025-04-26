package kr.hhplus.be.server.interfaces.common.aspect;

import kr.hhplus.be.server.domain.user.UserCommand;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.domain.user.point.PointHistory;
import kr.hhplus.be.server.domain.user.point.PointHistoryLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class PointHistoryAspect {

    private final UserRepository userRepository;
    public PointHistoryAspect(
            UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Around("@annotation(pointHistoryLog)")
    public Object logPointHistory(ProceedingJoinPoint joinPoint, PointHistoryLog pointHistoryLog) throws Throwable {
        Object result = joinPoint.proceed();

        Arrays.stream(joinPoint.getArgs())
                .filter(UserCommand.Charge.class::isInstance)
                .map(UserCommand.Charge.class::cast)
                .findFirst()
                .ifPresent(charge -> savePointHistory(charge, pointHistoryLog));

        return result;
    }

    private void savePointHistory(UserCommand.Charge charge, PointHistoryLog pointHistoryLog) {
        Long userId = charge.getUserId();
        Integer amount = charge.getAmount();

        if (userId == null || amount == null) return;

        PointHistory history = PointHistory.create(userId, pointHistoryLog.value(), amount);
        userRepository.savePointHistory(history);
    }
}
