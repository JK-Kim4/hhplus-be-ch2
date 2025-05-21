package kr.hhplus.be.server.integration.payment;

import kr.hhplus.be.server.application.port.DataPlatformPort;
import kr.hhplus.be.server.domain.payment.PaymentCompleteEvent;
import kr.hhplus.be.server.domain.payment.PaymentInfo;
import kr.hhplus.be.server.support.DummyEventPublisher;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@EnableAsync
@Testcontainers
public class PaymentEventTest {

    @Autowired
    DummyEventPublisher dummyEventPublisher;

    @MockitoBean
    DataPlatformPort dataPlatformPort;

    @Test
    void 결제_완료_이벤트가_성공적으로_호출된다() {
        // given
        PaymentCompleteEvent event = PaymentCompleteEvent.from(
                PaymentInfo.Complete.builder()
                        .paymentId(100L)
                        .orderId(1L)
                        .paidAmount(BigDecimal.TEN)
                        .paidAt(LocalDateTime.of(2022, 1,2,1,1))
                        .build()
        );

        // when: 트랜잭션 커밋 후 실행되도록 트랜잭션을 강제 시작/커밋
        dummyEventPublisher.event_발행(event);

        // then: Awaitility로 비동기 실행을 대기
        Awaitility.await()
                .atMost(Duration.ofSeconds(3))
                .untilAsserted(() ->
                        verify(dataPlatformPort, times(1)).sendPaymentData(event.toInfo())
                );
    }

}
