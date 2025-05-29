package kr.hhplus.be.server.domain.payment.event;

public interface PaymentEventPublisher {

    void complete(PaymentCompletedEvent event);

    void fail(PaymentCompletedEvent event);

}
