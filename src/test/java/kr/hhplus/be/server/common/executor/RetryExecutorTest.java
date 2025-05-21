package kr.hhplus.be.server.common.executor;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class RetryExecutorTest {

    @Test
    void 실행로직에_대하여_재시도횟수를_모두_소진시_예외가_발생한다(){
        //given
        RetryPolicy retryPolicy = RetryPolicy.of(3, Duration.ofMillis(100));
        AtomicInteger executionCount = new AtomicInteger(0);
        Runnable failingTask = () -> {
            executionCount.incrementAndGet();
            throwExceptionMethod();
        };

        //when/then
        assertThatThrownBy(() -> RetryExecutor.executeWithRetry(retryPolicy, failingTask))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Retry attempts exhausted");
    }

    private void throwExceptionMethod(){
        throw new IllegalStateException("테스트 예외 발생");
    }
}
