package kr.hhplus.be.server.support.kafka;

import kr.hhplus.be.server.domain.payment.event.PaymentCompletedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class TestProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    public TestProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(PaymentCompletedEvent event) {
        kafkaTemplate.send(TestConsumer.PAYMENT_COMPLETED_TOPIC_TEST, event);
    }

    public void fail(PaymentCompletedEvent event) {
        kafkaTemplate.send(TestConsumer.PAYMENT_COMPLETED_DLQ_TEST, event);
    }


}
