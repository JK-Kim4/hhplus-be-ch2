package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.common.annotation.DistributedLock;
import kr.hhplus.be.server.infrastructure.lock.LockExecutorType;
import kr.hhplus.be.server.domain.coupon.*;
import kr.hhplus.be.server.domain.coupon.couponApplicant.CouponApplicantInfo;
import kr.hhplus.be.server.domain.coupon.couponApplicant.CouponApplicantService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Transactional
public class CouponFacade {

    private final CouponService couponService;
    private final CouponApplicantService couponApplicantService;

    public CouponFacade(
            CouponService couponService,
            CouponApplicantService couponApplicantService) {
        this.couponService = couponService;
        this.couponApplicantService = couponApplicantService;
    }

    public CouponResult.RequestRegister requestRegister(CouponCriteria.RequestRegister criteria){
        couponApplicantService.validationRegister(criteria.toCommand());
        CouponApplicantInfo.RegisterApplicant registerApplicant = couponApplicantService.registerCouponApplicant(criteria.toRegisterCommand());

        return CouponResult.RequestRegister.from(registerApplicant);
    }

    public void deleteExpiredRedisCouponKeys(){
        CouponInfo.ExpiredCouponIds expiredCouponIds = couponService.getExpiredCouponIds();

        for(Long couponId : expiredCouponIds.getCouponIds()){
            //redisCouponService.deleteRedisCouponKey(CouponCommand.DeleteKey.of(couponId));
            couponApplicantService.deleteExpiredCouponKeys(CouponCommand.DeleteKey.of(couponId));
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

    public void issueCoupons() {
        CouponApplicantInfo.IssuableCoupons issuableCouponIds = couponApplicantService.findIssuableCouponIds();

        for(Long couponId: issuableCouponIds.getIssuableCouponIds()){
            Integer availableQuantity = couponService.getAvailableQuantityByCouponId(couponId);
            //CouponInfo.FetchFromRedis fetchFromRedis = redisCouponService.fetchApplicantsFromRedis(CouponCommand.FetchFromRedis.of(couponId, availableQuantity));
            CouponApplicantInfo.Applicants applicants = couponApplicantService.fetchApplicantUserIds(couponId);

            //couponService.issueCouponsToApplicants(couponId, fetchFromRedis.getApplicants());
            couponService.issueCouponToApplicantsV2(couponId, applicants.getUserIds());
        }

    }
}
