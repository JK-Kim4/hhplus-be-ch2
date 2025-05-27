package kr.hhplus.be.server.integration.kafka;

import kr.hhplus.be.server.common.event.PaymentCompletedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class KafkaTestPublisher {

    private final KafkaTemplate<String , Object> kafkaTemplate;

    public static final String PAYMENT_COMPLETED_TOPIC_TEST = "payment_completed_topic_test";

    public KafkaTestPublisher(KafkaTemplate<String , Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    public String event_발행(PaymentCompletedEvent event){
        kafkaTemplate.send(KafkaTestPublisher.PAYMENT_COMPLETED_TOPIC_TEST, event);

        return "OK";
    }
}
