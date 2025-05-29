package kr.hhplus.be.server.integration.kafka;

import kr.hhplus.be.server.domain.payment.PaymentInfo;
import kr.hhplus.be.server.domain.payment.event.PaymentCompletedEvent;
import kr.hhplus.be.server.domain.payment.event.PaymentEventPublisher;
import kr.hhplus.be.server.support.TestConsumer;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SpringBootTest
@Testcontainers
public class PaymentEventTest {

    @MockitoSpyBean
    PaymentEventPublisher paymentEventPublisher;

    @Autowired
    TestConsumer.PaymentCompletedEventConsumer testConsumer;

    @Test
    void 결제_완료_이벤트가_성공적으로_호출된다() {
        // given
        PaymentCompletedEvent event = PaymentCompletedEvent.from(
                PaymentInfo.Complete.builder()
                        .paymentId(100L)
                        .orderId(1L)
                        .paidAmount(BigDecimal.TEN)
                        .paidAt(LocalDateTime.of(2022, 1, 2, 1, 1))
                        .build()
        );

        // when: 트랜잭션 커밋 후 실행되도록 트랜잭션을 강제 시작/커밋
        paymentEventPublisher.send(event);

        // then: Awaitility로 비동기 실행을 대기
        Awaitility.await()
                .atMost(30, java.util.concurrent.TimeUnit.SECONDS)
                .pollInterval(3, java.util.concurrent.TimeUnit.SECONDS)
                .untilAsserted(() ->
                        Assertions.assertEquals(event, testConsumer.getReceivedMessages().get(0))
                );
    }
}
