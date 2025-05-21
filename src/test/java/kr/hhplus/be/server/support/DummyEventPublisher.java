package kr.hhplus.be.server.support;

import kr.hhplus.be.server.domain.payment.PaymentCompleteEvent;
import kr.hhplus.be.server.domain.payment.PaymentEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DummyEventPublisher {

    private final PaymentEventPublisher paymentEventPublisher;

    public DummyEventPublisher(PaymentEventPublisher paymentEventPublisher) {
        this.paymentEventPublisher = paymentEventPublisher;
    }

    @Transactional
    public String event_발행(PaymentCompleteEvent event){
        paymentEventPublisher.complete(event);

        return "OK";
    }
}
