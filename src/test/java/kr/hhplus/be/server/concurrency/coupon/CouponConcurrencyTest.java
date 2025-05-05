package kr.hhplus.be.server.concurrency.coupon;

import jakarta.persistence.NoResultException;
import kr.hhplus.be.server.application.coupon.CouponCommand;
import kr.hhplus.be.server.application.coupon.CouponCommandService;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.userCoupon.UserCouponRepository;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@Sql(scripts = {"/import_test_user.sql", "/import_test_coupon.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD )
@Testcontainers
public class CouponConcurrencyTest {

    @Autowired CouponCommandService couponCommandService;
    @Autowired CouponRepository couponRepository;
    @Autowired UserRepository userRepository;
    @Autowired UserCouponRepository userCouponRepository;

    private Coupon testCoupon;
    private List<User> users;
    private Integer initialQuantity;

    @BeforeEach
    void setUp() {
        testCoupon = couponRepository.findById(1L)
                .orElseThrow(() -> new NoResultException("샘플 쿠폰 없음"));
        users = userRepository.findAll();
        initialQuantity = testCoupon.getQuantity();
    }

    @Nested
    class 선착순_쿠폰_발급_동시성_제어_테스트 {

        @Test
        void DB_낙관적락_선착순_쿠폰발급_동시성_제어_테스트() throws InterruptedException {
            runConcurrentIssueTest("DB 비관적락 동시성 제어", (user)
                    -> couponCommandService.issue(CouponCommand.Issue.of(testCoupon.getId(), user.getId())));
        }

        @Test
        void redisson_락_선착순_쿠폰발급_동시성_테스트() throws InterruptedException {
            runConcurrentIssueTest("Redisson 분산락", (user)
                    -> couponCommandService.issueWithDistributeLock(CouponCommand.Issue.of(testCoupon.getId(), user.getId())));
        }

        @Test
        void spin_락_선착순_쿠폰발급_동시성_테스트() throws InterruptedException {
            runConcurrentIssueTest("SpinLock 분산락", (user)
                    -> couponCommandService.issueWithSpinLock(CouponCommand.Issue.of(testCoupon.getId(), user.getId())));
        }

    }

    private void runConcurrentIssueTest(String label, ThrowingConsumer<User> issueLogic) throws InterruptedException {
        List<Throwable> exceptions = Collections.synchronizedList(new ArrayList<>());
        CountDownLatch latch = new CountDownLatch(users.size());
        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (User user : users) {
            executor.execute(() -> {
                try {
                    issueLogic.accept(user);
                } catch (Throwable e) {
                    exceptions.add(e);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();
        couponRepository.flush();

        Coupon afterTest = couponRepository.findById(testCoupon.getId()).orElseThrow();
        int issuedCount = initialQuantity - afterTest.getQuantity();

        assertEquals(0, afterTest.getQuantity(), label + " - 남은 재고는 0이어야 한다");
        assertEquals(users.size(), issuedCount + exceptions.size(), label + " - 발급 성공 + 실패 수 = 요청 수");
        assertEquals(initialQuantity, userCouponRepository.countByCouponId(testCoupon.getId()), label + "실제 발급된 쿠폰 수는 쿠폰 수량과 동일해아한다");
    }

    @FunctionalInterface
    interface ThrowingConsumer<T> {
        void accept(T t) throws Exception;
    }
}
