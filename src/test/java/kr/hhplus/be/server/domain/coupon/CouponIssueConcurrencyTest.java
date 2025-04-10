package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.userCoupon.UserCoupon;
import kr.hhplus.be.server.interfaces.exception.InvalidAmountException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CouponIssueConcurrencyTest {

    @Test
    void concurrentCouponIssueTest() throws InterruptedException {
        int initialStock = 100;
        int numberOfThreads = 100;


        CouponTemplate couponTemplate = CouponTemplate.builder()
                .id(10L)
                .name("flatCoupon")
                .couponType(CouponType.FLAT)
                .remainingQuantity(new AtomicInteger(initialStock))
                .expireDateTime(LocalDateTime.of(2099, 1, 1, 0, 0))
                .build();
        // 임의 구현체 생성
        FlatDiscountCoupon coupon = new FlatDiscountCoupon(couponTemplate);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(numberOfThreads);

        ExecutorService executor = Executors.newFixedThreadPool(20);
        List<Future<UserCoupon>> results = new ArrayList<>();

        for (int i = 0; i < numberOfThreads; i++) {
            final int userId = i;
            Future<UserCoupon> future = executor.submit(() -> {
                try {
                    startLatch.await(); // 모든 스레드가 동시에 시작하게 대기
                    return coupon.issue(new User("user" + userId));
                } catch (InvalidAmountException e) {
                    // 품절로 인한 실패는 무시
                    return null;
                } finally {
                    doneLatch.countDown();
                }
            });
            results.add(future);
        }

        // 모든 스레드 시작
        startLatch.countDown();
        doneLatch.await(); // 모든 작업 완료 대기

        // 발급 성공한 쿠폰 수 확인
        long successCount = results.stream().filter(future -> {
            try {
                return future.get() != null;
            } catch (Exception e) {
                return false;
            }
        }).count();

        assertEquals(initialStock, successCount); // 성공한 발급 수는 재고 수와 같아야 함
        assertEquals(0, coupon.getRemainingQuantity().get()); // 재고는 0이어야 함

        executor.shutdown();
    }
}
