package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.coupon.userCoupon.UserCoupon;
import kr.hhplus.be.server.domain.coupon.userCoupon.UserCouponCriteria;
import kr.hhplus.be.server.domain.coupon.userCoupon.UserCouponInfo;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
public class CouponFacade {

    private final CouponService couponService;


    public CouponFacade(
            CouponService couponService) {
        this.couponService = couponService;
    }

    public UserCouponInfo.Issue issue(UserCouponCriteria.Issue criteria) {
        if(couponService.isAlreadyIssuedCoupon(criteria.getCouponId(), criteria.getUserId())){
            throw new IllegalArgumentException("이미 발급된 쿠폰입니다.");
        }

        UserCoupon userCoupon = couponService.issue(criteria.getCouponId(), criteria.getUserId());

        userCoupon = couponService.saveUserCoupon(userCoupon);

        return UserCouponInfo.Issue.of(
                userCoupon.getId(),
                userCoupon.getCoupon().getId(),
                userCoupon.getUser().getId(),
                userCoupon.getIssueDateTime());
    }

    @Transactional(readOnly = true)
    public UserCouponInfo.UserCouponList findByUserId(Long userId) {
        List<UserCoupon> couponList = couponService.findByUserId(userId);
        return UserCouponInfo.UserCouponList.of(couponList);
    }

}
