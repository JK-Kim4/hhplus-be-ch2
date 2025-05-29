package kr.hhplus.be.server.infrastructure.payment.event;

import kr.hhplus.be.server.domain.payment.event.PaymentCompletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/*
* SpringPaymentEventPublisher
* 애플리케이션 이벤트 발행 책임
* */
@Component
public class SpringPaymentEventPublisher {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ApplicationEventPublisher applicationEventPublisher;

    public SpringPaymentEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void complete(PaymentCompletedEvent event) {
        applicationEventPublisher.publishEvent(event);
    }

}
