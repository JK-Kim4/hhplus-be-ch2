package kr.hhplus.be.server.integration.coupon;

import kr.hhplus.be.server.domain.coupon.*;
import kr.hhplus.be.server.domain.redis.RedisKeys;
import kr.hhplus.be.server.domain.redis.RedisZSetStore;
import kr.hhplus.be.server.domain.salesStat.TypedScore;
import kr.hhplus.be.server.support.DatabaseCleanup;
import kr.hhplus.be.server.support.domainSupport.CouponDomainSupporter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Testcontainers
public class CouponServiceTest {

    @MockitoSpyBean
    @Autowired
    CouponService couponService;

    @Autowired
    RedisCouponService redisCouponService;

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    RedisZSetStore redisZSetStore;

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

        redisZSetStore.incrementScore(RedisKeys.COUPON_REQUEST_ISSUE.format(coupon1.getId()), "1", System.currentTimeMillis());
        redisZSetStore.incrementScore(RedisKeys.COUPON_REQUEST_ISSUE.format(coupon1.getId()), "2", System.currentTimeMillis());
        redisZSetStore.incrementScore(RedisKeys.COUPON_REQUEST_ISSUE.format(coupon1.getId()), "3", System.currentTimeMillis());
        redisZSetStore.incrementScore(RedisKeys.COUPON_REQUEST_ISSUE.format(coupon1.getId()), "4", System.currentTimeMillis());
        redisZSetStore.incrementScore(RedisKeys.COUPON_REQUEST_ISSUE.format(coupon2.getId()), "4", System.currentTimeMillis());
        redisZSetStore.incrementScore(RedisKeys.COUPON_REQUEST_ISSUE.format(coupon2.getId()), "1", System.currentTimeMillis());
        redisZSetStore.incrementScore(RedisKeys.COUPON_REQUEST_ISSUE.format(coupon2.getId()), "2", System.currentTimeMillis());

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

    @Test
    void Redis_저장소에_선착순_쿠폰발급_요청자정보를_조회한다(){
        //when
        CouponInfo.FetchFromRedis fetchFromRedis = redisCouponService.fetchApplicantsFromRedis(CouponCommand.FetchFromRedis.of(coupon1.getId(), couponService.getAvailableQuantityByCouponId(coupon1.getId())));
        //then
        assertEquals(4, fetchFromRedis.getApplicants().size());
        Assertions.assertThat(fetchFromRedis.getApplicants().stream().map(TypedScore::member).toList())
                .contains("1", "2", "3", "4");
    }

    @Test
    void 쿠폰_고유번호와_Redis_저장소_발급요청자_목록을_전달받아_쿠폰을_발급한다(){
        //when
        CouponInfo.FetchFromRedis fetchFromRedis = redisCouponService.fetchApplicantsFromRedis(CouponCommand.FetchFromRedis.of(coupon1.getId(), couponService.getAvailableQuantityByCouponId(coupon1.getId())));
        couponService.issueCouponsToApplicants(coupon1.getId(), fetchFromRedis.getApplicants());

        //then
        verify(couponService, times(fetchFromRedis.getApplicants().size())).issueUserCoupon(any(CouponCommand.Issue.class));
    }
}
