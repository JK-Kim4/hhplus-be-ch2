package kr.hhplus.be.server.infrastructure.payment.event;

import kr.hhplus.be.server.application.payment.event.PaymentEventPublisher;
import kr.hhplus.be.server.common.event.PaymentCompletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

/*
* SpringPaymentEventPublisher
* 애플리케이션 이벤트 발행 책임
* */
public class SpringPaymentEventPublisher implements PaymentEventPublisher {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ApplicationEventPublisher applicationEventPublisher;

    public SpringPaymentEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void complete(PaymentCompletedEvent event) {
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void fail(PaymentCompletedEvent event) {
        logger.error("Payment Failed: {}", event.toString());
    }
}
