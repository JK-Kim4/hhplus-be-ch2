package kr.hhplus.be.server.common.aspect;

import kr.hhplus.be.server.common.TransactionSynchronizer;
import kr.hhplus.be.server.domain.history.exception.ExceptionHistory;
import kr.hhplus.be.server.domain.history.exception.ExceptionHistoryService;
import kr.hhplus.be.server.support.DummyService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@Testcontainers
public class ExceptionHistoryAspectTest {

    private ExceptionHistoryService exceptionHistoryService;
    private DummyService dummyService;

    @BeforeEach
    void setUp() {
        exceptionHistoryService = mock(ExceptionHistoryService.class);

        // AspectJ 프록시 구성
        ExceptionHistoryAspect aspect = new ExceptionHistoryAspect(exceptionHistoryService);
        DummyService target = new DummyService();

        AspectJProxyFactory factory = new AspectJProxyFactory(target);
        factory.addAspect(aspect);
        dummyService = factory.getProxy();
    }

    @Test
    void ExceptionRecordable_애노테이션이_작성된_로직에서_예외가_발생할경우_이력저장_AOP_실행() {
        String param1 = "testParam1";
        String param2 = "testParam2";

        try (MockedStatic<TransactionSynchronizer> mocked = mockStatic(TransactionSynchronizer.class)) {
            mocked.when(() -> TransactionSynchronizer.runAfterCommit(any()))
                    .thenAnswer(invocation -> {
                        Runnable runnable = invocation.getArgument(0);
                        runnable.run();  // 즉시 실행
                        return null;
                    });

            Assertions.assertThrows(IllegalArgumentException.class, ()
                    -> dummyService.ExceptionRecordable_String_parameter_테스트(param1, param2));

            verify(exceptionHistoryService, times(1))
                    .saveExceptionHistory(any(LocalDate.class), any(ExceptionHistory.class));
        }
    }


}
