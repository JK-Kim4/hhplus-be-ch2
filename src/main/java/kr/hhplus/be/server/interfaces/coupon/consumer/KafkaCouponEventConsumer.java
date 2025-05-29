package kr.hhplus.be.server.interfaces.coupon.consumer;

import kr.hhplus.be.server.application.coupon.event.CouponEventPublisher;
import kr.hhplus.be.server.application.coupon.event.CouponEventService;
import kr.hhplus.be.server.common.event.CouponIssueRequestedEvent;
import kr.hhplus.be.server.common.keys.kafka.KafkaConsumerGroups;
import kr.hhplus.be.server.common.keys.kafka.KafkaTopics;
import kr.hhplus.be.server.domain.coupon.CouponCommand;
import kr.hhplus.be.server.domain.coupon.CouponService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaCouponEventConsumer {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final CouponService couponService;
    private final CouponEventService couponEventService;
    private final CouponEventPublisher couponEventPublisher;

    public KafkaCouponEventConsumer(
            CouponService couponService,
            CouponEventService couponEventService,
            CouponEventPublisher couponEventPublisher) {
        this.couponService = couponService;
        this.couponEventService = couponEventService;
        this.couponEventPublisher = couponEventPublisher;
    }

    @KafkaListener(
            topics = KafkaTopics.COUPON_ISSUE_REQUESTED_TOPIC,
            groupId = KafkaConsumerGroups.COUPON_ISSUE_REQUESTED_GROUP,
            concurrency = "2"
    )
    public void listen(CouponIssueRequestedEvent event){
        //멱등성 보장
        if(couponEventService.hasIssueRequestedIdempotencyKey(event)){
            logger.info("Idempotency key already exists. Event: {}", event);
            return;
        }

        try {
            couponService.issueUserCoupon(CouponCommand.Issue.of(event.getCouponId(), event.getUserId()));
            couponEventService.saveIssueRequestedIdempotencyKey(event);
        }catch (Exception e){
            couponEventPublisher.fail(event);
            logger.error("Failed to coupon issue request event. Sent to DLQ. Event: {}, Error: {}", event, e.getMessage(), e);
        }
    }


}
