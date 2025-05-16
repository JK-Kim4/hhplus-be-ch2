package kr.hhplus.be.server.common.aspect;

import kr.hhplus.be.server.application.payment.PaymentCriteria;
import kr.hhplus.be.server.support.DummyService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Testcontainers
public class TrackSalesAOPTest {

    @MockitoSpyBean
    private SalesStatAspect salesStatAspect;

    @Autowired
    DummyService service;

    @Test
    void 비지니스로직_정상적으로_return_되지않으면_AOP가_실행되지않는다(){
        //given
        PaymentCriteria.Pay pay = PaymentCriteria.Pay.of(100L, 1L);

        //when
        assertThrows(RuntimeException.class, () -> service.TrackSales_결제_오류발생_테스트(pay));

        //then
        verify(salesStatAspect, times(0)).trackSales(any(ProceedingJoinPoint.class));
    }
}
