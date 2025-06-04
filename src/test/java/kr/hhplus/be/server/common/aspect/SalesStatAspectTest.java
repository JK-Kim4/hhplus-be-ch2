package kr.hhplus.be.server.common.aspect;

import kr.hhplus.be.server.application.payment.PaymentCriteria;
import kr.hhplus.be.server.application.salesstat.SalesStatFacade;
import kr.hhplus.be.server.common.executor.TransactionExecutor;
import kr.hhplus.be.server.support.DummyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SalesStatAspectTest {

    private SalesStatFacade salesStatFacade;
    private DummyService dummyService;

    @BeforeEach
    void setUp() {
        salesStatFacade = mock(SalesStatFacade.class);

        // AspectJ 프록시 구성
        SalesStatAspect aspect = new SalesStatAspect(salesStatFacade);
        DummyService target = new DummyService();

        AspectJProxyFactory factory = new AspectJProxyFactory(target);
        factory.addAspect(aspect);
        dummyService = factory.getProxy();
    }

    @Test
    void 트랜잭션커밋후_통계처리를_호출한다() {
        PaymentCriteria.Pay pay = PaymentCriteria.Pay.of(1L, 1L);

        try (MockedStatic<TransactionExecutor> mocked = mockStatic(TransactionExecutor.class)) {
            mocked.when(() -> TransactionExecutor.runAfterCommit(any()))
                    .thenAnswer(invocation -> {
                        Runnable runnable = invocation.getArgument(0);
                        runnable.run();  // 즉시 실행
                        return null;
                    });

            dummyService.TrackSales_결제_테스트(pay);

            verify(salesStatFacade, times(1))
                    .dailySalesReportProcess(1L, LocalDate.now());
        }
    }
}
