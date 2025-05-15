package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.domain.redis.*;
import kr.hhplus.be.server.domain.salesStat.TypedScore;
import org.springframework.stereotype.Service;

@Service
public class RedisCouponService {

    private final RedisZSetStore<TypedScore> redisZSetStore;
    private final RedisCommonStore redisCommonStore;
    private final RedisSetStore redisSetStore;
    private final RedisMapStore redisMapStore;

    public RedisCouponService(
            RedisZSetStore<TypedScore> redisZSetStore,
            RedisSetStore redisSetStore,
            RedisMapStore redisMapStore,
            RedisCommonStore redisCommonStore) {
        this.redisZSetStore = redisZSetStore;
        this.redisCommonStore = redisCommonStore;
        this.redisSetStore = redisSetStore;
        this.redisMapStore = redisMapStore;
    }

    public CouponInfo.FetchFromRedis fetchApplicantsFromRedis(CouponCommand.FetchFromRedis command) {
        String redisKey = RedisKeys.COUPON_REQUEST_ISSUE.format(command.getCouponId());
        return CouponInfo.FetchFromRedis.of(redisZSetStore.rangeWithScores(redisKey, 0, command.getQuantity()));
    }

    public CouponInfo.RequestIssue insertUserIdInIssuanceSet(CouponCommand.Issue command){
        long requestTimeMillis = System.currentTimeMillis();

        redisZSetStore.add(
                RedisKeys.COUPON_REQUEST_ISSUE.format(command.getCouponId()),
                command.getUserId().toString(),
                requestTimeMillis);

        return CouponInfo.RequestIssue.of(requestTimeMillis, command.getUserId(), command.getCouponId());
    }

    public void validationCouponRequest(CouponCommand.Issue command){
        isKeyAvailable(command);
        isAlreadyRequestUser(command);
    }

    public void deleteRedisCouponKey(CouponCommand.DeleteKey command){
        redisCommonStore.removeWithKeys(RedisKeys.getCouponKeys(command.getCouponId()));
    }

    public void isAlreadyRequestUser(CouponCommand.Issue command) {
        if(redisZSetStore.getScore(
                        RedisKeys.COUPON_REQUEST_ISSUE.format(command.getCouponId()),
                        command.getUserId().toString())
                .isPresent()){
            throw new IllegalArgumentException("발급 요청 처리중입니다.");
        }
    }

    public void isKeyAvailable(CouponCommand.Issue command) {
        if(!redisCommonStore.hasKey(RedisKeys.COUPON_ISSUABLE_FLAG.format(command.getCouponId()))){
            throw new IllegalArgumentException("유효하지 않은 쿠폰 번호입니다.");
        }
    }
}
