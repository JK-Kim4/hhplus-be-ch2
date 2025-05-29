package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.application.port.DataPlatformPort;
import kr.hhplus.be.server.common.StringUtil;
import kr.hhplus.be.server.domain.payment.event.PaymentCompletedEvent;
import kr.hhplus.be.server.domain.retryfailedlog.RetryFailedLog;
import kr.hhplus.be.server.domain.retryfailedlog.RetryFailedLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PaymentDataSender {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final DataPlatformPort dataPlatformPort;
    private final RetryFailedLogService retryFailedLogService;

    public PaymentDataSender(
            DataPlatformPort dataPlatformPort,
            RetryFailedLogService retryFailedLogService) {
        this.dataPlatformPort = dataPlatformPort;
        this.retryFailedLogService = retryFailedLogService;
    }


    @Retryable(
            value = Exception.class,
            maxAttempts = 4,
            backoff = @Backoff(delay = 300)
    )
    public void sendWithRetry(PaymentCompletedEvent event) {
        dataPlatformPort.sendPaymentData(event);
    }

    @Recover
    public void recover(Exception e, PaymentCompletedEvent event) {
        logger.error("sendPaymentData failed. event={}", event, e);
        RetryFailedLog log = new RetryFailedLog(
                "sendWithRetry",
                event.getClass().getName(),
                StringUtil.toJson(event),
                e.getMessage(),
                e.getClass().getName(),
                LocalDateTime.now()
        );

        retryFailedLogService.save(log);
    }
}
