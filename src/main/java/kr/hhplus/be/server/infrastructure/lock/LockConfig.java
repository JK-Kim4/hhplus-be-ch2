package kr.hhplus.be.server.infrastructure.lock;

import java.util.concurrent.TimeUnit;

public record LockConfig(
        long waitTime,
        long leaseTime,
        TimeUnit timeUnit,
        int mayRetry,
        long retryIntervalMillis
) {
    
}
