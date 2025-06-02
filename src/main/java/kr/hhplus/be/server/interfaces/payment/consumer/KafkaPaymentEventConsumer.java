package kr.hhplus.be.server.interfaces.payment.consumer;

import kr.hhplus.be.server.application.payment.PaymentDataSender;
import kr.hhplus.be.server.application.payment.event.PaymentEventPublisher;
import kr.hhplus.be.server.application.payment.event.PaymentEventService;
import kr.hhplus.be.server.common.event.PaymentCompletedEvent;
import kr.hhplus.be.server.common.keys.kafka.KafkaConsumerGroups;
import kr.hhplus.be.server.common.keys.kafka.KafkaTopics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaPaymentEventConsumer {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PaymentDataSender paymentDataSender;
    private final PaymentEventPublisher paymentEventPublisher;
    private final PaymentEventService paymentEventService;

    public KafkaPaymentEventConsumer(
            PaymentDataSender paymentDataSender,
            PaymentEventPublisher paymentEventPublisher,
            PaymentEventService paymentEventService) {
        this.paymentDataSender = paymentDataSender;
        this.paymentEventPublisher = paymentEventPublisher;
        this.paymentEventService = paymentEventService;
    }

    @KafkaListener(
            topics = KafkaTopics.PAYMENT_COMPLETED_TOPIC,
            groupId = KafkaConsumerGroups.PAYMENT_COMPLETE_GROUP)
    public void listen(PaymentCompletedEvent event) {

        if(paymentEventService.hasPaymentCompletedIdempotencyKey(event)){
            logger.info("Idempotency key already exists. Event: {}", event);
            return;
        }

        try {
            paymentDataSender.sendWithRetry(event);
            paymentEventService.savePaymentCompletedIdempotencyKey(event);
        }catch (Exception e){
            // DLQ 전송
            paymentEventPublisher.fail(event);
            // 필요 시 로그 기록
            logger.error("Failed to process payment event. Sent to DLQ. Event: {}, Error: {}", event, e.getMessage(), e);
        }
    }
}
