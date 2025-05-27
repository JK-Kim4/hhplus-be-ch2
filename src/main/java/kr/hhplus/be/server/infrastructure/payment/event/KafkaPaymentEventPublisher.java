package kr.hhplus.be.server.infrastructure.payment.event;

import kr.hhplus.be.server.application.payment.event.PaymentEventPublisher;
import kr.hhplus.be.server.common.keys.kafka.KafkaTopics;
import kr.hhplus.be.server.common.event.PaymentCompletedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaPaymentEventPublisher implements PaymentEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaPaymentEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void complete(PaymentCompletedEvent event) {
        kafkaTemplate.send(KafkaTopics.PAYMENT_COMPLETED_TOPIC, event);
    }

    @Override
    public void fail(PaymentCompletedEvent event) {
        kafkaTemplate.send(KafkaTopics.PAYMENT_COMPLETED_DLQ, event);
    }
}
