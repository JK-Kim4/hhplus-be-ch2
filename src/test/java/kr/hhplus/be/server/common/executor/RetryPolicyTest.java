package kr.hhplus.be.server.common.executor;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RetryPolicyTest {

    @Test
    void 재시도_정책_객체를_생성한다(){
        //given
        int maxRetries = 3;
        Duration delay = Duration.ofMillis(100);

        //when
        RetryPolicy retryPolicy = RetryPolicy.of(maxRetries, delay);

        //then
        assertAll("재시도 정책 생성",
                () -> assertEquals(maxRetries, retryPolicy.maxRetries()),
                () -> assertEquals(delay, retryPolicy.delay())
        );
    }
}
