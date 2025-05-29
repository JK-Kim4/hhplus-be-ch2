package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.application.coupon.event.CouponEventPublisher;
import kr.hhplus.be.server.common.annotation.DistributedLock;
import kr.hhplus.be.server.common.event.CouponIssueRequestedEvent;
import kr.hhplus.be.server.domain.coupon.CouponCommand;
import kr.hhplus.be.server.domain.coupon.CouponInfo;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.coupon.couponApplicant.CouponApplicantInfo;
import kr.hhplus.be.server.domain.coupon.couponApplicant.CouponApplicantService;
import kr.hhplus.be.server.infrastructure.lock.LockExecutorType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Transactional
public class CouponFacade {

    private final CouponService couponService;
    private final CouponApplicantService couponApplicantService;
    private final CouponEventPublisher couponEventPublisher;

    public CouponFacade(
            CouponService couponService,
            CouponApplicantService couponApplicantService,
            CouponEventPublisher couponEventPublisher) {
        this.couponService = couponService;
        this.couponApplicantService = couponApplicantService;
        this.couponEventPublisher = couponEventPublisher;
    }

    public CouponResult.Create create(CouponCriteria.Create criteria){
        CouponInfo.Create create = couponService.create(criteria.toCommand());

        couponApplicantService.registerCouponKeys(create.getCouponId());

        return CouponResult.Create.from(create);
    }

    public CouponResult.UserCoupons findUserCouponsByUserId(CouponCriteria.UserCoupon criteria){
        CouponInfo.UserCoupons userCoupons = couponService.findUserCouponByUserId(criteria.getUserId());
        return CouponResult.UserCoupons.from(userCoupons);
    }

    public CouponResult.RequestRegister requestRegister(CouponCriteria.RequestRegister criteria){
        couponApplicantService.validationRegister(criteria.toCommand());
        CouponApplicantInfo.RegisterApplicant registerApplicant = couponApplicantService.registerCouponApplicant(criteria.toRegisterCommand());

        return CouponResult.RequestRegister.from(registerApplicant);
    }

    public void deleteExpiredRedisCouponKeys(){
        CouponInfo.ExpiredCouponIds expiredCouponIds = couponService.getExpiredCouponIds();

        for(Long couponId : expiredCouponIds.getCouponIds()){
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

    public void issueV2(CouponCriteria.Issue criteria){
        couponApplicantService.validationIssuableCoupon(criteria.toCommand());

        couponApplicantService.contains(criteria.toCommand());

        CouponIssueRequestedEvent event = CouponIssueRequestedEvent.of(criteria.getCouponId(), criteria.getUserId());

        couponEventPublisher.send(event);
    }

    public void issueCoupons() {
        CouponApplicantInfo.IssuableCoupons issuableCouponIds = couponApplicantService.findIssuableCouponIds();

        for(Long couponId: issuableCouponIds.getIssuableCouponIds()){
            Integer availableQuantity = couponService.getAvailableQuantityByCouponId(couponId);
            CouponApplicantInfo.Applicants applicants = couponApplicantService.fetchApplicantUserIds(couponId);
            couponService.issueCouponToApplicantsV2(couponId, applicants.getUserIds());
        }

    }
}
