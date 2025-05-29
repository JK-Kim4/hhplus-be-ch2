package kr.hhplus.be.server.integration.kafka;

import kr.hhplus.be.server.domain.payment.event.PaymentCompletedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class KafkaTestConsumer {

    private final List<PaymentCompletedEvent> receivedMessages = new ArrayList<>();

    @KafkaListener(
            topics = KafkaTestPublisher.PAYMENT_COMPLETED_TOPIC_TEST,
            groupId = "test-group"
    )
    public void listen(PaymentCompletedEvent message) {
        receivedMessages.add(message);
    }

    public List<PaymentCompletedEvent> getReceivedMessages() {
        return receivedMessages;
    }

    public void clearMessages() {
        receivedMessages.clear();
    }

}
