package kr.hhplus.be.server.concurrency.coupon;

import kr.hhplus.be.server.concurrency.support.ConcurrentTestExecutor;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.coupon.CouponTestFixture;
import kr.hhplus.be.server.infrastructure.coupon.InMemoryCouponIssueQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
public class CouponConcurrencyTest {

    @Autowired
    CouponService couponService;

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    InMemoryCouponIssueQueue inMemoryCouponIssueQueue;

    Coupon testCoupon;

    @BeforeEach
    void setUp() {
        testCoupon = CouponTestFixture
                .createCouponFixtureWithQuantityAndDiscountPrice(4, 5_000);

        couponRepository.save(testCoupon);
    }

    @Test
    void 쿠폰_재고감소_동시성_테스트() throws InterruptedException {

        List<Runnable> tasks = Arrays.asList(
                () -> inMemoryCouponIssueQueue.enqueue(testCoupon.getId(),
                        UUID.randomUUID().toString().substring(0, 6)),
                () -> inMemoryCouponIssueQueue.enqueue(testCoupon.getId(),
                        UUID.randomUUID().toString().substring(0, 6)),
                () -> inMemoryCouponIssueQueue.enqueue(testCoupon.getId(),
                        UUID.randomUUID().toString().substring(0, 6)),
                () -> inMemoryCouponIssueQueue.enqueue(testCoupon.getId(),
                        UUID.randomUUID().toString().substring(0, 6)),
                () -> inMemoryCouponIssueQueue.enqueue(testCoupon.getId(),
                        UUID.randomUUID().toString().substring(0, 6))
        );

        ConcurrentTestExecutor.execute(10, tasks);

        while (inMemoryCouponIssueQueue.size() > 0) {
            Thread.sleep(100);
        }

        couponRepository.flush();
        Coupon coupon = couponRepository.findById(testCoupon.getId()).orElseThrow();


        assertEquals(0, coupon.getQuantity());
    }
}
