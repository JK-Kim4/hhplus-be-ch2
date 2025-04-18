package kr.hhplus.be.server.integration.coupon;

import kr.hhplus.be.server.domain.coupon.*;
import kr.hhplus.be.server.infrastructure.coupon.FlatDiscountCouponJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
public class CouponServiceTest {


    @Autowired
    CouponService couponService;

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    FlatDiscountCouponJpaRepository flatDiscountCouponJpaRepository;


    @Test
    @Transactional
    void 쿠폰을_저장한다(){
        Coupon coupon = new Coupon("test", CouponType.FLAT, 10,
                LocalDate.of(2025, 12, 31), LocalDate.of(2025,1,1));

        //when
        couponService.saveCoupon(coupon, 50_000);
        Coupon coupon2 = couponRepository.findById(coupon.getId()).get();
        FlatDiscountCoupon flatDiscountCoupon = flatDiscountCouponJpaRepository.findByCouponId(coupon2.getId());

        //then
        assertEquals(coupon2.getFlatDiscountCoupon(), flatDiscountCoupon);
        assertEquals(50_000, flatDiscountCoupon.getDiscountAmount());
    }


}