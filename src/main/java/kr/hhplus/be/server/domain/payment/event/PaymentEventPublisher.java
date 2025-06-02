package kr.hhplus.be.server.domain.payment.event;

public interface PaymentEventPublisher {

    void send(PaymentCompletedEvent event);

    void fail(PaymentCompletedEvent event);

}
