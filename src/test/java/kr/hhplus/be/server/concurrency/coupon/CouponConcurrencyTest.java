package kr.hhplus.be.server.concurrency.coupon;

import kr.hhplus.be.server.concurrency.support.ConcurrentTestExecutor;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.coupon.CouponTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
public class CouponConcurrencyTest {

    @Autowired
    CouponService couponService;

    @Autowired
    CouponRepository couponRepository;

    Coupon testCoupon;

    @BeforeEach
    void setUp() {
        testCoupon = CouponTestFixture
                .createCouponFixtureWithQuantityAndDiscountPrice(50, 5_000);

        couponRepository.save(testCoupon);
    }

    @Test
    void 쿠폰_재고감소_동시성_테스트() throws InterruptedException {

        List<Runnable> tasks = Arrays.asList(
                () -> couponService.deductCouponQuantity(testCoupon.getId())
        );

        ConcurrentTestExecutor.execute(50, 10, tasks);
        couponRepository.flush();
        Coupon coupon = couponRepository.findById(testCoupon.getId()).orElseThrow();


        assertEquals(40, coupon.getQuantity());
    }
}
