package kr.hhplus.be.server.application.coupon;


import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.coupon.DiscountPolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDate;

@SpringBootTest
@Testcontainers
public class CouponServiceTest {

    @Autowired
    CouponService couponService;

    @Autowired
    CouponRepository couponRepository;

    Coupon testCoupon;

    @BeforeEach
    void setup(){
        Coupon coupon = Coupon.create(
                "쿠폰 발급 동시성 테스트",
                5,
                DiscountPolicy.FLAT,
                BigDecimal.valueOf(5000),
                LocalDate.now().plusDays(3)
        );

        testCoupon = couponRepository.save(coupon);
    }

    @Test
    void 쿠폰이_발급되면_수량이_차감된다(){

    }


}
