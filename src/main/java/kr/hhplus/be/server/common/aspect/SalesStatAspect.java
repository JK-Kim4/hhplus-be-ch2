package kr.hhplus.be.server.common.aspect;

import kr.hhplus.be.server.application.payment.PaymentCriteria;
import kr.hhplus.be.server.application.salesstat.SalesStatFacade;
import kr.hhplus.be.server.common.executor.TransactionExecutor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Aspect
@Component
public class SalesStatAspect {

    private final SalesStatFacade salesStatFacade;

    public SalesStatAspect(SalesStatFacade salesStatFacade) {
        this.salesStatFacade = salesStatFacade;
    }

    @AfterReturning(pointcut = "@annotation(kr.hhplus.be.server.common.annotation.TrackSales)")
    public void trackSales(JoinPoint joinPoint) {
        PaymentCriteria.Pay pay = extractRequiredPaymentCriteria(joinPoint);
        //비지니스 로직이 정상적으로 수행 && 트랜잭션 종료 이후 결과 반영
        TransactionExecutor.runAfterCommit(() -> {
            salesStatFacade.dailySalesReportProcess(pay.getOrderId(), LocalDate.now());
        });
    }

    private PaymentCriteria.Pay extractRequiredPaymentCriteria(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();

        if (args.length == 0 || !(args[0] instanceof PaymentCriteria.Pay criteria)) {
            throw new IllegalArgumentException("PaymentCriteria.Pay argument is required");
        }

        return criteria;
    }

}
