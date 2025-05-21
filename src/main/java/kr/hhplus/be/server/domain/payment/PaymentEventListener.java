package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.application.port.DataPlatformPort;
import kr.hhplus.be.server.common.annotation.ExceptionRecordable;
import kr.hhplus.be.server.common.executor.RetryExecutor;
import kr.hhplus.be.server.common.executor.RetryPolicy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.Duration;

@Component
public class PaymentEventListener {

    private final DataPlatformPort dataPlatformPort;

    public PaymentEventListener(DataPlatformPort dataPlatformPort) {
        this.dataPlatformPort = dataPlatformPort;
    }

    @Async
    @ExceptionRecordable(retryable = true)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePaymentCompleteEvent(PaymentCompleteEvent event) {
        RetryExecutor.executeWithRetry(RetryPolicy.of(3, Duration.ofMillis(100)),
                () -> dataPlatformPort.sendPaymentData(event.toInfo()));
    }
}
