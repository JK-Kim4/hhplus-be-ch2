package kr.hhplus.be.server.support.kafka;

import kr.hhplus.be.server.common.event.CouponIssueRequestedEvent;
import kr.hhplus.be.server.domain.payment.event.PaymentCompletedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

public class TestConsumer {

    public static final String PAYMENT_COMPLETED_TOPIC_TEST = "payment.completed.topic.test";
    public static final String PAYMENT_COMPLETED_DLQ_TEST = "payment.completed.dlq";
    public static final String COUPON_ISSUE_REQUESTED_TOPIC_TEST = "coupon.issue.requested.test";

    @Component
    public static class PaymentCompletedEventConsumer{

        private final List<PaymentCompletedEvent> receivedMessages = new ArrayList<>();

        @KafkaListener(
                topics = TestConsumer.PAYMENT_COMPLETED_TOPIC_TEST,
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
                topics = TestConsumer.COUPON_ISSUE_REQUESTED_TOPIC_TEST,
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
