package kr.hhplus.be.server.application.port;

import kr.hhplus.be.server.domain.payment.PaymentInfo;

public interface DataPlatformPort {

    void sendPaymentData(PaymentInfo.Complete payment);

}
