package kr.hhplus.be.server.infrastructure.payment;

import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentDataPlatformClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class PaymentDataPlatformClientImpl implements PaymentDataPlatformClient {

    @Override
    public void sendPaymentData(Payment payment) {
        log.info("sendPaymentData: {}", payment);
    }
}
