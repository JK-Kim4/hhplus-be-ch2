package kr.hhplus.be.server.integration.coupon;

import kr.hhplus.be.server.domain.couponv2.*;
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
    CouponV2Service couponV2Service;

    @Autowired
    CouponV2Repository couponV2Repository;

    @Autowired
    FlatDiscountCouponJpaRepository flatDiscountCouponJpaRepository;


    @Test
    @Transactional
    void 쿠폰을_저장한다(){
        CouponV2 coupon = new CouponV2("test", CouponType.FLAT, 10,
                LocalDate.of(2025, 12, 31), LocalDate.of(2025,1,1));

        //when
        couponV2Service.saveCoupon(coupon, 50_000);
        CouponV2 coupon2 = couponV2Repository.findById(coupon.getId()).get();
        FlatDiscountCouponV2 flatDiscountCoupon = flatDiscountCouponJpaRepository.findByCouponId(coupon2.getId());

        //then
        assertEquals(coupon2.getFlatDiscountCoupon(), flatDiscountCoupon);
        assertEquals(50_000, flatDiscountCoupon.getDiscountAmount());
    }


}