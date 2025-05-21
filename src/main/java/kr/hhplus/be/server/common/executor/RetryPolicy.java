package kr.hhplus.be.server.common.executor;

import lombok.Builder;

import java.time.Duration;

public class RetryPolicy {

    private final int maxRetries;
    private final Duration delay;

    public static RetryPolicy of(int maxRetries, Duration delay) {
        return RetryPolicy.builder().maxRetries(maxRetries).delay(delay).build();
    }

    @Builder
    private RetryPolicy(int maxRetries, Duration delay) {
        this.maxRetries = maxRetries;
        this.delay = delay;
    }

    public int maxRetries() {
        return maxRetries;
    }

    public Duration delay() {
        return delay;
    }

    public void waitBeforeRetry() {
        try {
            Thread.sleep(delay.toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Retry interrupted", e);
        }
    }

}
