package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.application.port.DataPlatformPort;
import kr.hhplus.be.server.common.event.PaymentCompletedEvent;
import kr.hhplus.be.server.common.executor.RetryExecutor;
import kr.hhplus.be.server.common.executor.RetryPolicy;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class PaymentDataSender {

    private final DataPlatformPort dataPlatformPort;

    public PaymentDataSender(DataPlatformPort dataPlatformPort) {
        this.dataPlatformPort = dataPlatformPort;
    }

    public void sendWithRetry(PaymentCompletedEvent event) {
        RetryExecutor.executeWithRetry(
                RetryPolicy.of(3, Duration.ofMillis(100)),
                () -> dataPlatformPort.sendPaymentData(event)
        );
    }
}
