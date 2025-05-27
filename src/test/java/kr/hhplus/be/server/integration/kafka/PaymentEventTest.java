package kr.hhplus.be.server.integration.kafka;

import kr.hhplus.be.server.common.event.PaymentCompletedEvent;
import kr.hhplus.be.server.config.KafkaTestContainerConfig;
import kr.hhplus.be.server.domain.payment.PaymentInfo;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

@SpringBootTest
@Testcontainers
public class PaymentEventTest {

    @Autowired
    KafkaTemplate<String ,Object> kafkaTemplate;

    @Autowired
    KafkaTestConsumer kafkaTestConsumer;

    @BeforeEach
    void setUp() {

        try (AdminClient adminClient = KafkaTestContainerConfig.getAdminClient()) {
            NewTopic topic = new NewTopic(KafkaTestPublisher.PAYMENT_COMPLETED_TOPIC_TEST, 1, (short) 1);
            adminClient.createTopics(Collections.singletonList(topic)).all().get();
        } catch (ExecutionException e) {
            if (e.getCause() instanceof org.apache.kafka.common.errors.TopicExistsException) {
                System.out.println("Topic already exists: " + KafkaTestPublisher.PAYMENT_COMPLETED_TOPIC_TEST);
            } else {
                throw new RuntimeException("Failed to initialize Kafka topic: " + KafkaTestPublisher.PAYMENT_COMPLETED_TOPIC_TEST, e);
            }
        } catch (Exception e) {
            throw new RuntimeException("Kafka admin error", e);
        }
    }

    @Test
    void 결제_완료_이벤트가_성공적으로_호출된다() {
        // given
        PaymentCompletedEvent event = PaymentCompletedEvent.from(
                PaymentInfo.Complete.builder()
                        .paymentId(100L)
                        .orderId(1L)
                        .paidAmount(BigDecimal.TEN)
                        .paidAt(LocalDateTime.of(2022, 1,2,1,1))
                        .build()
        );

        // when: 트랜잭션 커밋 후 실행되도록 트랜잭션을 강제 시작/커밋
        kafkaTemplate.send(KafkaTestPublisher.PAYMENT_COMPLETED_TOPIC_TEST, event);

        // then: Awaitility로 비동기 실행을 대기
        Awaitility.await()
                .atMost(Duration.ofSeconds(3))
                .untilAsserted(() ->
                        Assertions.assertEquals(event, kafkaTestConsumer.getReceivedMessages().get(0))
                );
    }

}
