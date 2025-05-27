package kr.hhplus.be.server.interfaces.coupon.scheduler;

import kr.hhplus.be.server.application.coupon.CouponFacade;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Profile("prod")
@Transactional
public class CouponScheduler {

    private final CouponFacade couponFacade;

    public CouponScheduler(CouponFacade couponFacade) {
        this.couponFacade = couponFacade;
    }

    //쿠폰 발급 짧은 주기 배치 (1초 간격)
    @Scheduled(cron = "*/1 * * * * *")
    public void issue(){
        couponFacade.issueCoupons();
    }

    //일 배치 쿠폰 캐시 Key flush
    @Scheduled(cron = "0 0 1 * * *")
    public void deleteExpired(){
        couponFacade.deleteExpiredRedisCouponKeys();
    }
}
