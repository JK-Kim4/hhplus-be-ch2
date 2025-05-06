package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.domain.coupon.CouponInfo;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.interfaces.common.annotation.DistributedLock;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Transactional
public class CouponFacade {

    private final CouponService couponService;

    public CouponFacade(CouponService couponService) {
        this.couponService = couponService;
    }

    @DistributedLock(prefix = "couponId", key = "#criteria.couponId")
    public CouponResult.Issue issue(CouponCriteria.Issue criteria){
        CouponInfo.Coupon coupon
                = couponService.findByIdWithPessimisticLock(criteria.getCouponId());


        Optional<UserCoupon> userCouponOptional
                = couponService.findUserCouponByCouponIdAndUserId(coupon.getCoupon().getId(), criteria.getUserId()).getUserCoupon();
        if(userCouponOptional.isPresent()){
            throw new IllegalArgumentException("이미 발급된 쿠폰입니다.");
        }

        CouponInfo.Issue result = couponService.issueUserCoupon(criteria.toCommand());

        return CouponResult.Issue.from(result);
    }
}
