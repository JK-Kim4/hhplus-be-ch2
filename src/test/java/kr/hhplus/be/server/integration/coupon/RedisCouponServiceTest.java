package kr.hhplus.be.server.integration.coupon;

import kr.hhplus.be.server.domain.coupon.CouponCommand;
import kr.hhplus.be.server.domain.coupon.CouponInfo;
import kr.hhplus.be.server.domain.coupon.RedisCouponService;
import kr.hhplus.be.server.domain.redis.RedisCommonStore;
import kr.hhplus.be.server.domain.redis.RedisKeys;
import kr.hhplus.be.server.domain.redis.RedisZSetStore;
import kr.hhplus.be.server.domain.salesStat.TypedScore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
public class RedisCouponServiceTest {

    @Autowired
    RedisCouponService redisCouponService;

    @Autowired
    RedisZSetStore<TypedScore> redisZSetStore;

    @Autowired
    RedisCommonStore redisCommonStore;

    @Autowired
    RedisTemplate redisTemplate;

    Long couponId = 1L;
    Long existUserId = 2L;

    @BeforeEach
    void setup(){테스트_초기화();}


    @Test
    void Redis에_쿠폰_발급요청_KEY가_존재하지않을경우_오류반환(){
        //given
        Long nonExistCouponId = 99L;
        CouponCommand.Issue issue = CouponCommand.Issue.of(nonExistCouponId, 1L);

        //when
        IllegalArgumentException illegalArgumentException =
                assertThrows(IllegalArgumentException.class, () -> redisCouponService.isKeyAvailable(issue));

        //then
        assertEquals("유효하지 않은 쿠폰 번호입니다.", illegalArgumentException.getMessage());
    }

    @Test
    void Redis에_쿠폰_발급요청_KEY가_존재하고_이미_신청이_완료된_사용자의_경우_오류반환(){
        //given
        CouponCommand.Issue issue = CouponCommand.Issue.of(couponId, existUserId);

        //when
        IllegalArgumentException illegalArgumentException =
                assertThrows(IllegalArgumentException.class, () -> redisCouponService.isAlreadyRequestUser(issue));

        //then
        assertEquals("발급 요청 처리중입니다.", illegalArgumentException.getMessage());
    }

    @Test
    void 발급요청이_정상적으로_처리되면_Redis_Key_member에_사용지고유번호가_추가된다(){
        //given
        Long requestUserId = 99L;
        CouponCommand.Issue command = CouponCommand.Issue.of(couponId, requestUserId);

        //when
        CouponInfo.RequestIssue requestIssue = redisCouponService.insertUserIdInIssuanceSet(command);
        Optional<Double> score = redisZSetStore.getScore(RedisKeys.COUPON_REQUEST_ISSUE.format(couponId), requestUserId.toString());

        //then
        assertNotNull(requestIssue.getRequestTimeMillis());
        assertEquals(requestUserId, requestIssue.getUserId());
        assertTrue(score.isPresent());
    }

    @Test
    void 만료된_쿠폰의_고유번호를_전달받아_Redis_키를_모두_삭제한다(){
        //given
        Long 만료_쿠폰_고유번호 = 99L;
        Long 활성_쿠폰_고유번호 = 100L;
        redisCommonStore.writeSimpleKey(RedisKeys.COUPON_ISSUABLE_FLAG.format(만료_쿠폰_고유번호));
        redisZSetStore.add(RedisKeys.COUPON_REQUEST_ISSUE.format(만료_쿠폰_고유번호), "1", System.currentTimeMillis());
        redisCommonStore.writeSimpleKey(RedisKeys.COUPON_ISSUABLE_FLAG.format(활성_쿠폰_고유번호));
        redisZSetStore.add(RedisKeys.COUPON_REQUEST_ISSUE.format(활성_쿠폰_고유번호), "1", System.currentTimeMillis());
        assertAll("키 정상 등록 확인",
                    () -> assertTrue(redisCommonStore.hasKey(RedisKeys.COUPON_ISSUABLE_FLAG.format(만료_쿠폰_고유번호))),
                    () -> assertTrue(redisCommonStore.hasKey(RedisKeys.COUPON_REQUEST_ISSUE.format(만료_쿠폰_고유번호))),
                    () -> assertTrue(redisCommonStore.hasKey(RedisKeys.COUPON_ISSUABLE_FLAG.format(활성_쿠폰_고유번호))),
                    () -> assertTrue(redisCommonStore.hasKey(RedisKeys.COUPON_REQUEST_ISSUE.format(활성_쿠폰_고유번호)))
                );

        //when
        redisCouponService.deleteRedisCouponKey(CouponCommand.DeleteKey.of(만료_쿠폰_고유번호));

        //then
        assertAll("키 삭제 검증",
                    () -> assertFalse(redisCommonStore.hasKey(RedisKeys.COUPON_ISSUABLE_FLAG.format(만료_쿠폰_고유번호))),
                    () -> assertFalse(redisCommonStore.hasKey(RedisKeys.COUPON_REQUEST_ISSUE.format(만료_쿠폰_고유번호))),
                    () -> assertTrue(redisCommonStore.hasKey(RedisKeys.COUPON_ISSUABLE_FLAG.format(활성_쿠폰_고유번호))),
                    () -> assertTrue(redisCommonStore.hasKey(RedisKeys.COUPON_REQUEST_ISSUE.format(활성_쿠폰_고유번호)))
                );


    }


    private void 테스트_초기화(){
        redisTemplate.execute((RedisConnection connection) -> {
            connection.flushAll();
            return null;
        });

        redisCommonStore.writeSimpleKey(RedisKeys.COUPON_ISSUABLE_FLAG.format(couponId));

        redisZSetStore.add(
                RedisKeys.COUPON_REQUEST_ISSUE.format(couponId),
                existUserId.toString(),
                System.currentTimeMillis());
    }
}
