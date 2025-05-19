package kr.hhplus.be.server.integration.coupon;

import kr.hhplus.be.server.common.redis.RedisKeys;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponInfo;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.coupon.couponApplicant.CouponApplicantInMemoryRepository;
import kr.hhplus.be.server.support.DatabaseCleanup;
import kr.hhplus.be.server.support.domainSupport.CouponDomainSupporter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

    Coupon coupon1;
    Coupon coupon2;
    Coupon coupon3;

    @BeforeEach
    void setup() {

        databaseCleanup.truncate();


        coupon1 = CouponDomainSupporter.수량정보를_전달받아_유효_테스트쿠폰_생성(10);
        coupon2 = CouponDomainSupporter.수량정보를_전달받아_유효_테스트쿠폰_생성(1);
        coupon3 = CouponDomainSupporter.수량정보를_전달받아_유효_테스트쿠폰_생성(0);

        couponRepository.saveAll(List.of(coupon1, coupon2, coupon3));
        couponRepository.save(CouponDomainSupporter.기간만료_쿠폰_생성());

        RedisKeys.COUPON_REQUEST_ISSUE.format(coupon1.getId());
        RedisKeys.COUPON_REQUEST_ISSUE.format(coupon2.getId());
        RedisKeys.COUPON_REQUEST_ISSUE.format(coupon3.getId());


        couponApplicantInMemoryRepository.registerCouponApplicant(coupon1.getId(), 1L, System.currentTimeMillis());
        couponApplicantInMemoryRepository.registerCouponApplicant(coupon1.getId(), 2L, System.currentTimeMillis());
        couponApplicantInMemoryRepository.registerCouponApplicant(coupon1.getId(), 3L, System.currentTimeMillis());
        couponApplicantInMemoryRepository.registerCouponApplicant(coupon1.getId(), 4L, System.currentTimeMillis());
        couponApplicantInMemoryRepository.registerCouponApplicant(coupon2.getId(), 1L, System.currentTimeMillis());
        couponApplicantInMemoryRepository.registerCouponApplicant(coupon2.getId(), 2L, System.currentTimeMillis());

    }


    @Test
    void 발급가능_잔여수량이_존재하고_만료기간_이전인_쿠폰의_목록을_조회한다(){
        CouponInfo.AvailableCouponIds issuableCouponIds = couponService.getIssuableCouponIds();

        assertEquals(2, issuableCouponIds.getCouponIds().size());

    }

    @Test
    void 고유번호에_해당하는_쿠폰의_발급가능수량을_조회한다(){
        //when
        Integer coupon1Quantity = couponService.getAvailableQuantityByCouponId(coupon1.getId());
        Integer coupon2Quantity = couponService.getAvailableQuantityByCouponId(coupon2.getId());

        //then
        assertAll("쿠폰 잔여 수량 조회",
                () -> assertEquals(10, coupon1Quantity),
                () -> assertEquals(1, coupon2Quantity));
    }
}
