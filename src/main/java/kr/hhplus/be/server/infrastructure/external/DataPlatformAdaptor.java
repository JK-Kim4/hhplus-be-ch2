package kr.hhplus.be.server.infrastructure.external;

import kr.hhplus.be.server.application.port.DataPlatformPort;
import kr.hhplus.be.server.domain.payment.PaymentInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DataPlatformAdaptor implements DataPlatformPort {

    @Override
    public void sendPaymentData(PaymentInfo.Complete payment) {
        log.info("sendPaymentData: {}", payment.toString());
    }
}
