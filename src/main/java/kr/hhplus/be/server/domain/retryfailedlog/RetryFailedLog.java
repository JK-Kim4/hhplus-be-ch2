package kr.hhplus.be.server.domain.retryfailedlog;

import java.time.LocalDateTime;

public record RetryFailedLog(
        String methodName,
        String parameterType,
        String parameterJson,
        String exceptionMessage,
        String exceptionClass,
        LocalDateTime occurredAt
) {
}
