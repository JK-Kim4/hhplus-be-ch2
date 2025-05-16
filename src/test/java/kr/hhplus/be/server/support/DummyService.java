package kr.hhplus.be.server.support;

import kr.hhplus.be.server.application.payment.PaymentCriteria;
import kr.hhplus.be.server.common.annotation.TrackSales;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DummyService {

    @TrackSales
    @Transactional
    public String TrackSales_결제_테스트(PaymentCriteria.Pay criteria) {
        System.out.println("💰 결제 수행 완료: orderId = " + criteria.getOrderId());
        return "OK";
    }

    @TrackSales
    @Transactional
    public String TrackSales_결제_오류발생_테스트(PaymentCriteria.Pay criteria){
        this.throwException();
        return "OK";
    }

    private void throwException(){
        throw new RuntimeException("오류 발생 테스트");
    }

}
