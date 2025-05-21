package kr.hhplus.be.server.common.executor;

public class RetryExecutor {

    public static void executeWithRetry(RetryPolicy retryPolicy, Runnable task) {

        int attempt = 0;
        while (attempt <= retryPolicy.maxRetries()) {
            try {
                task.run();
                return; // 성공 시 종료
            } catch (Exception e) {
                attempt++;
                if (attempt > retryPolicy.maxRetries()) {
                    throw new RuntimeException("Retry attempts exhausted", e);
                }
                retryPolicy.waitBeforeRetry();
            }
        }
        throw new IllegalStateException("Unexpected error in retry logic");
    }
}
