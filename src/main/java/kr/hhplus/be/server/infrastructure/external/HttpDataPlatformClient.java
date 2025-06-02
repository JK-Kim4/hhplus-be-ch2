package kr.hhplus.be.server.infrastructure.external;

import kr.hhplus.be.server.application.port.DataPlatformPort;
import kr.hhplus.be.server.common.event.PaymentCompletedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HttpDataPlatformClient implements DataPlatformPort {

    @Override
    public void sendPaymentData(PaymentCompletedEvent event) {
        log.info("sendPaymentData: {}", event.toString());
    }
}
