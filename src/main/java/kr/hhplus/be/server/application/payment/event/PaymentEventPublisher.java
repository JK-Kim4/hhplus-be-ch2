package kr.hhplus.be.server.application.payment.event;

import kr.hhplus.be.server.common.event.PaymentCompletedEvent;

public interface PaymentEventPublisher {

    void complete(PaymentCompletedEvent event);

    void fail(PaymentCompletedEvent event);

}
