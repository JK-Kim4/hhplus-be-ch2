package kr.hhplus.be.server.integration.coupon;

import kr.hhplus.be.server.common.keys.CacheKeys;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponInfo;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.coupon.couponApplicant.CouponApplicantInMemoryRepository;
import kr.hhplus.be.server.support.DatabaseCleanup;
import kr.hhplus.be.server.support.domain.CouponDomainSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
public class CouponServiceTest {

    @MockitoSpyBean
    @Autowired
    CouponService couponService;

    @Autowired
    CouponApplicantInMemoryRepository couponApplicantInMemoryRepository;

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    DatabaseCleanup databaseCleanup;

    @Autowired
    PlatformTransactionManager transactionManager;


    Coupon coupon1;
    Coupon coupon2;
    Coupon coupon3;

    @BeforeEach
    void setup() {
        databaseCleanup.truncate();
        쿠폰_테스트데이터_셋업();
    }


    @Test
    void 발급가능_잔여수량이_존재하고_만료기간_이전인_쿠폰의_목록을_조회한다(){
        CouponInfo.AvailableCouponIds issuableCouponIds = couponService.getIssuableCouponIds();

        assertEquals(2, issuableCouponIds.getCouponIds().size());

    }

    @Test
    @Transactional
    void 고유번호에_해당하는_쿠폰의_발급가능수량을_조회한다(){
        //when
        Integer coupon1Quantity = couponService.getAvailableQuantityByCouponId(coupon1.getId());
        Integer coupon2Quantity = couponService.getAvailableQuantityByCouponId(coupon2.getId());

        //then
        assertAll("쿠폰 잔여 수량 조회",
                () -> assertEquals(10, coupon1Quantity),
                () -> assertEquals(1, coupon2Quantity));
    }

    @Test
    void 쿠폰조회_비관적락_획득시_반납이전까지_해당_데이터에_접근이_불가능하다() throws Exception {

        CountDownLatch tx1LockAcquired = new CountDownLatch(1);
        CountDownLatch tx1Release = new CountDownLatch(1);
        CountDownLatch tx2Finished = new CountDownLatch(1);
        AtomicLong tx2ElapsedMillis = new AtomicLong();

        // 트랜잭션1: 락을 잡고 일정 시간동안 보유
        Thread tx1 = new Thread(() -> {
            TransactionTemplate txTemplate = new TransactionTemplate(transactionManager);
            txTemplate.executeWithoutResult(status -> {
                Coupon coupon = couponRepository.findByIdWithPessimisticLock(coupon1.getId())
                        .orElseThrow();
                tx1LockAcquired.countDown(); // 락 잡았다고 알림
                try {
                    tx1Release.await(); // 락 보유 상태 유지
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        });

        // 트랜잭션2: 동일 row에 접근 시도 (락 대기 발생 기대)
        Thread tx2 = new Thread(() -> {
            try {
                tx1LockAcquired.await(); // 트랜잭션1이 락을 잡을 때까지 대기
                long start = System.currentTimeMillis();

                TransactionTemplate txTemplate = new TransactionTemplate(transactionManager);
                txTemplate.setTimeout(5); // 5초 제한
                txTemplate.executeWithoutResult(status -> {
                    couponRepository.findByIdWithPessimisticLock(coupon1.getId());
                });

                long end = System.currentTimeMillis();
                tx2ElapsedMillis.set(end - start);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                tx2Finished.countDown();
            }
        });

        tx1.start();
        tx2.start();

        // 3초 후 락 해제
        Thread.sleep(3000);
        tx1Release.countDown();

        tx2Finished.await(); // 트랜잭션2가 끝날 때까지 대기

        // 실제로 대기한 시간이 3초 이상이면 락에 의해 block된 것으로 간주
        assertTrue(tx2ElapsedMillis.get() >= 2900, "비관적 락으로 인한 대기 시간이 충분히 길지 않음");
    }


    private void 쿠폰_테스트데이터_셋업(){
        coupon1 = CouponDomainSupport.수량정보를_전달받아_유효_테스트쿠폰_생성(10);
        coupon2 = CouponDomainSupport.수량정보를_전달받아_유효_테스트쿠폰_생성(1);
        coupon3 = CouponDomainSupport.수량정보를_전달받아_유효_테스트쿠폰_생성(0);

        couponRepository.saveAll(List.of(coupon1, coupon2, coupon3));
        couponRepository.save(CouponDomainSupport.기간만료_쿠폰_생성());

        CacheKeys.COUPON_REQUEST_ISSUE.format(coupon1.getId());
        CacheKeys.COUPON_REQUEST_ISSUE.format(coupon2.getId());
        CacheKeys.COUPON_REQUEST_ISSUE.format(coupon3.getId());


        couponApplicantInMemoryRepository.registerCouponApplicant(coupon1.getId(), 1L, System.currentTimeMillis());
        couponApplicantInMemoryRepository.registerCouponApplicant(coupon1.getId(), 2L, System.currentTimeMillis());
        couponApplicantInMemoryRepository.registerCouponApplicant(coupon1.getId(), 3L, System.currentTimeMillis());
        couponApplicantInMemoryRepository.registerCouponApplicant(coupon1.getId(), 4L, System.currentTimeMillis());
        couponApplicantInMemoryRepository.registerCouponApplicant(coupon2.getId(), 1L, System.currentTimeMillis());
        couponApplicantInMemoryRepository.registerCouponApplicant(coupon2.getId(), 2L, System.currentTimeMillis());
    }
}
