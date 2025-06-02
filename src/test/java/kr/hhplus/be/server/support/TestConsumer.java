package kr.hhplus.be.server.support;

import kr.hhplus.be.server.common.event.CouponIssueRequestedEvent;
import kr.hhplus.be.server.common.keys.kafka.KafkaTopics;
import kr.hhplus.be.server.domain.payment.event.PaymentCompletedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

public class TestConsumer {

    public static final String PAYMENT_COMPLETED_TOPIC_TEST = "payment-completed-topic-test";

    @Component
    public static class PaymentCompletedEventConsumer{

        private final List<PaymentCompletedEvent> receivedMessages = new ArrayList<>();

        @KafkaListener(
                topics = KafkaTopics.PAYMENT_COMPLETED_TOPIC,
                groupId = "test-payment-complete-group"
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


    @Component
    public static class CouponIssueRequestedEventConsumer{

        private final List<CouponIssueRequestedEvent> receivedMessages = new ArrayList<>();

        @KafkaListener(
                topics = KafkaTopics.COUPON_ISSUE_REQUESTED_TOPIC,
                groupId = "test-coupon-issue-group"
        )
        public void listen(CouponIssueRequestedEvent message) {
            receivedMessages.add(message);
        }

        public List<CouponIssueRequestedEvent> getReceivedMessages() {
            return receivedMessages;
        }

        public void clearMessages() {
            receivedMessages.clear();
        }

    }


}
