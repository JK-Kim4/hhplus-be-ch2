package kr.hhplus.be.server.infrastructure.payment;

import kr.hhplus.be.server.domain.payment.PaymentCompleteEvent;
import kr.hhplus.be.server.domain.payment.PaymentEventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventPublisherImpl implements PaymentEventPublisher {

    ApplicationEventPublisher applicationEventPublisher;

    public PaymentEventPublisherImpl(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void complete(PaymentCompleteEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
}
