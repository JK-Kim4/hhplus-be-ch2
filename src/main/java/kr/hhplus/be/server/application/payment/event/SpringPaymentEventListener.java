package kr.hhplus.be.server.application.payment.event;

import kr.hhplus.be.server.domain.payment.event.PaymentCompletedEvent;
import kr.hhplus.be.server.domain.payment.event.PaymentEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class SpringPaymentEventListener {

    private final PaymentEventPublisher paymentEventPublisher;

    public SpringPaymentEventListener(PaymentEventPublisher paymentEventPublisher) {
        this.paymentEventPublisher = paymentEventPublisher;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePaymentCompleteEvent(PaymentCompletedEvent event) {
        paymentEventPublisher.send(event);
    }
}
