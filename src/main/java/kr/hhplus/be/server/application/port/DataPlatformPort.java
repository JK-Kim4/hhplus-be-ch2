package kr.hhplus.be.server.application.port;


import kr.hhplus.be.server.common.event.PaymentCompletedEvent;

public interface DataPlatformPort {

    void sendPaymentData(PaymentCompletedEvent event);

}
