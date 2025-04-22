package kr.hhplus.be.server.application.coupon;

import jakarta.persistence.NoResultException;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.coupon.userCoupon.UserCoupon;
import kr.hhplus.be.server.domain.coupon.userCoupon.UserCouponCriteria;
import kr.hhplus.be.server.domain.coupon.userCoupon.UserCouponInfo;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@Transactional
public class CouponFacade {

    private final CouponService couponService;

    public CouponFacade(CouponService couponService) {
        this.couponService = couponService;
    }

    public UserCouponInfo.Issue issue(UserCouponCriteria.Issue criteria) {
        couponService.findByCouponIdAndUserId(criteria.getCouponId(), criteria.getUserId())
                .ifPresent(uc -> new IllegalArgumentException("이미 발급된 쿠폰입니다."));

        UserCoupon userCoupon =
                Optional.of(couponService.issue(criteria.getCouponId(), criteria.getUserId()))
                        .map(couponService::saveUserCoupon)
                        .orElseThrow(NoResultException::new);

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
