package kr.hhplus.be.server.integration.payment;

import kr.hhplus.be.server.application.payment.PaymentDataSender;
import kr.hhplus.be.server.application.port.DataPlatformPort;
import kr.hhplus.be.server.domain.payment.event.PaymentCompletedEvent;
import kr.hhplus.be.server.domain.retryfailedlog.RetryFailedLogService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SpringBootTest
@Testcontainers
public class PaymentDataSenderTest {

    @MockitoBean
    DataPlatformPort dataPlatformPort;

    @MockitoSpyBean
    RetryFailedLogService retryFailedLogService;

    @Autowired
    PaymentDataSender paymentDataSender;

    @Test
    void 데이터플랫폼_전송시_재시도전략에_실패하면_복구로직이_실행된다(){
        //given
        PaymentCompletedEvent event = PaymentCompletedEvent.of(
                1L,
                10L,
                BigDecimal.valueOf(10000L),
                LocalDateTime.of(2023, 10, 1, 12, 0)
        );

        Mockito.doThrow(new RuntimeException("Timeout occurred"))
                .when(dataPlatformPort).sendPaymentData(event);

        //when
        paymentDataSender.sendWithRetry(event);

        //then
        Mockito.verify(dataPlatformPort, Mockito.times(4)).sendPaymentData(event);
        Mockito.verify(retryFailedLogService, Mockito.times(1)).save(Mockito.any());
    }


}
