package kr.hhplus.be.server.concurrent.coupon;

import kr.hhplus.be.server.application.coupon.CouponCriteria;
import kr.hhplus.be.server.application.coupon.CouponFacade;
import kr.hhplus.be.server.support.ConcurrentTestExecutor;
import kr.hhplus.be.server.domain.coupon.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
public class CouponIssueConcurrentTest {

    @Autowired
    CouponService couponService;

    @Autowired
    CouponFacade couponFacade;

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
    void 수량이_5개인_쿠폰에대해_10명이_동시_발급요청할경우_5장만_발급되어야한다() throws InterruptedException {

        List<Runnable> tasks = Arrays.asList(
                () -> couponFacade.issue(CouponCriteria.Issue.builder().couponId(testCoupon.getId()).userId(1L).build()),
                () -> couponFacade.issue(CouponCriteria.Issue.builder().couponId(testCoupon.getId()).userId(2L).build()),
                () -> couponFacade.issue(CouponCriteria.Issue.builder().couponId(testCoupon.getId()).userId(3L).build()),
                () -> couponFacade.issue(CouponCriteria.Issue.builder().couponId(testCoupon.getId()).userId(4L).build()),
                () -> couponFacade.issue(CouponCriteria.Issue.builder().couponId(testCoupon.getId()).userId(5L).build()),
                () -> couponFacade.issue(CouponCriteria.Issue.builder().couponId(testCoupon.getId()).userId(6L).build()),
                () -> couponFacade.issue(CouponCriteria.Issue.builder().couponId(testCoupon.getId()).userId(7L).build()),
                () -> couponFacade.issue(CouponCriteria.Issue.builder().couponId(testCoupon.getId()).userId(8L).build()),
                () -> couponFacade.issue(CouponCriteria.Issue.builder().couponId(testCoupon.getId()).userId(9L).build()),
                () -> couponFacade.issue(CouponCriteria.Issue.builder().couponId(testCoupon.getId()).userId(10L).build())
        );


        List<Throwable> execute = ConcurrentTestExecutor.execute(5, tasks);

        couponRepository.flush();
        Coupon coupon = couponRepository.findById(testCoupon.getId()).get();

        System.out.println("남은 쿠폰 잔여 수: " + coupon.getQuantity() +", 발생 오류 수: " + execute.size());

        assertThat(coupon.getQuantity()).isEqualTo(0);
        assertThat(execute.size()).isEqualTo(5);

    }
}
