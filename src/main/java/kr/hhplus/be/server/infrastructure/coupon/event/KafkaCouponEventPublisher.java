package kr.hhplus.be.server.infrastructure.coupon.event;

import kr.hhplus.be.server.application.coupon.event.CouponEventPublisher;
import kr.hhplus.be.server.common.event.CouponIssueRequestedEvent;
import kr.hhplus.be.server.common.keys.kafka.KafkaTopics;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaCouponEventPublisher implements CouponEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    public KafkaCouponEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(CouponIssueRequestedEvent event) {
        kafkaTemplate.send(KafkaTopics.COUPON_ISSUE_REQUESTED_TOPIC, event);
    }

    @Override
    public void fail(CouponIssueRequestedEvent event) {
        kafkaTemplate.send(KafkaTopics.COUPON_ISSUE_REQUESTED_DLQ, event);
    }
}
