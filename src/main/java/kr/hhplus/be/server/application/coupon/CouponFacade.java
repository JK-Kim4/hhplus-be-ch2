package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.common.annotation.DistributedLock;
import kr.hhplus.be.server.common.lock.LockExecutorType;
import kr.hhplus.be.server.domain.coupon.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Transactional
public class CouponFacade {

    private final CouponService couponService;
    private final RedisCouponService redisCouponService;

    public CouponFacade(
            CouponService couponService,
            RedisCouponService redisCouponService) {
        this.couponService = couponService;
        this.redisCouponService = redisCouponService;
    }

    public CouponResult.RequestIssue requestIssue(CouponCriteria.Issue criteria){
        redisCouponService.validationCouponRequest(criteria.toCommand());
        CouponInfo.RequestIssue requestIssue = redisCouponService.insertUserIdInIssuanceSet(criteria.toCommand());

        return CouponResult.RequestIssue.from(requestIssue);
    }

    public void issueCouponsFromRedisApplicants(){
        //발급 가능 쿠폰 Id 조회
        CouponInfo.AvailableCouponIds availableCouponIds = couponService.getIssuableCouponIds();

        //발급 가능 쿠폰이 존재하지 않을 경우 탈출
        if(availableCouponIds.getCouponIds().isEmpty()){return;}

        //Redis Member 조회
        for(Long couponId : availableCouponIds.getCouponIds()){
            Integer availableQuantity = couponService.getAvailableQuantityByCouponId(couponId);
            CouponInfo.FetchFromRedis fetchFromRedis = redisCouponService.fetchApplicantsFromRedis(CouponCommand.FetchFromRedis.of(couponId, availableQuantity));

            couponService.issueCouponsToApplicants(couponId, fetchFromRedis.getApplicants());
        }
    }

    public void deleteExpiredRedisCouponKeys(){
        CouponInfo.ExpiredCouponIds expiredCouponIds = couponService.getExpiredCouponIds();

        for(Long couponId : expiredCouponIds.getCouponIds()){
            redisCouponService.deleteRedisCouponKey(CouponCommand.DeleteKey.of(couponId));
        }
    }

    @DistributedLock(prefix = "couponId", key = "#criteria.couponId", executor = LockExecutorType.PUBSUB)
    public CouponResult.Issue issue(CouponCriteria.Issue criteria){

        CouponInfo.Coupon coupon =
            couponService.findByIdWithPessimisticLock(criteria.getCouponId());


        Optional<UserCoupon> userCouponOptional =
            couponService.findUserCouponByCouponIdAndUserId(coupon.getCouponId(), criteria.getUserId()).getUserCoupon();

        if(userCouponOptional.isPresent()){
            throw new IllegalArgumentException("이미 발급된 쿠폰입니다.");
        }

        CouponInfo.Issue result = couponService.issueUserCoupon(criteria.toCommand());

        return CouponResult.Issue.from(result);
    }
}
