package kr.hhplus.be.server.infrastructure.coupon;

import kr.hhplus.be.server.common.keys.CacheKeys;
import kr.hhplus.be.server.domain.coupon.couponApplicant.CouponApplicant;
import kr.hhplus.be.server.domain.coupon.couponApplicant.CouponApplicantInMemoryRepository;
import org.redisson.api.RBucket;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.redisson.client.protocol.ScoredEntry;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CouponApplicantRedisRepositoryImpl implements CouponApplicantInMemoryRepository {

    private final RedissonClient redissonClient;

    public CouponApplicantRedisRepositoryImpl(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public void registerCouponApplicant(Long couponId, Long userId, long requestTimeMillis) {
        RScoredSortedSet<Long> CouponApplicants = redissonClient.getScoredSortedSet(CacheKeys.COUPON_REQUEST_ISSUE.format(couponId));
        CouponApplicants.addScore(userId, requestTimeMillis);
    }

    @Override
    public boolean containsCouponApplicant(Long couponId, Long userId) {
        RScoredSortedSet<Long> CouponApplicants = redissonClient.getScoredSortedSet(CacheKeys.COUPON_REQUEST_ISSUE.format(couponId));
        return CouponApplicants.contains(userId);
    }

    @Override
    public boolean existIssuableCoupon(Long couponId) {
        return redissonClient.getKeys().countExists(CacheKeys.COUPON_ISSUABLE_FLAG.format(couponId)) > 0;
    }

    @Override
    public List<Long> findIssuableCouponIds() {
        RScoredSortedSet<Long> scoredSortedSet = redissonClient.getScoredSortedSet(CacheKeys.COUPON_ISSUABLE_ID_SET.name());
        return scoredSortedSet.entryRange(0, -1).stream()
                .map(ScoredEntry::getValue)
                .toList();
    }

    @Override
    public void deleteIssuableCouponKey(Long couponId) {
        redissonClient.getKeys().delete(CacheKeys.COUPON_ISSUABLE_FLAG.format(couponId));
        RSet<Long> rSet = redissonClient.getSet(CacheKeys.COUPON_ISSUABLE_ID_SET.name());
        rSet.remove(couponId);
    }

    @Override
    public void deleteCouponApplicantKey(Long couponId) {
        redissonClient.getScoredSortedSet(CacheKeys.COUPON_REQUEST_ISSUE.format(couponId)).delete();
    }

    @Override
    public Optional<CouponApplicant> findByCouponIdAndUserId(Long couponId, Long userId) {
        RScoredSortedSet<Long> couponApplicantSet = redissonClient.getScoredSortedSet(CacheKeys.COUPON_REQUEST_ISSUE.format(couponId));
        Double score = couponApplicantSet.getScore(userId);
        return Optional.of(CouponApplicant.of(userId, couponId, score.longValue()));
    }

    @Override
    public Optional<CouponApplicant> findWinnerByCouponIdAndUserId(Long couponId, Long userId) {
        RScoredSortedSet<Object> couponApplicantSet = redissonClient.getScoredSortedSet(CacheKeys.COUPON_WINNER.format(couponId));
        Double score = couponApplicantSet.getScore(userId);
        return Optional.of(CouponApplicant.of(userId, couponId, score.longValue()));
    }

    @Override
    public List<CouponApplicant> findByCouponId(Long couponId) {
        RScoredSortedSet<Long> couponApplicantSet = redissonClient.getScoredSortedSet(CacheKeys.COUPON_REQUEST_ISSUE.format(couponId));
        return couponApplicantSet.entryRange(0, -1).stream()
                .map(entry -> CouponApplicant.of(entry.getValue(), couponId, entry.getScore().longValue()))
                .toList();
    }

    @Override
    public void registerCouponIssuableKey(Long couponId) {
        RBucket<String> issuableKey = redissonClient.getBucket(CacheKeys.COUPON_ISSUABLE_FLAG.format(couponId));
        issuableKey.set(CacheKeys.COUPON_ISSUABLE_FLAG.format(couponId));
    }
}
