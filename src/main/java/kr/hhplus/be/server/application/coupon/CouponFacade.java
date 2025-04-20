package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.coupon.userCoupon.UserCoupon;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.domain.coupon.userCoupon.UserCouponCriteria;
import kr.hhplus.be.server.domain.coupon.userCoupon.UserCouponInfo;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@Transactional
public class CouponFacade {

    private final CouponService couponService;
    private final UserService userService;

    public CouponFacade(
            CouponService couponService,
            UserService userService) {
        this.couponService = couponService;
        this.userService = userService;
    }

    public UserCouponInfo.Issue issue(UserCouponCriteria.Issue criteria) {
        Coupon coupon = couponService.findById(criteria.getCouponId());
        User user = userService.findById(criteria.getUserId());

        if(couponService.isAlreadyIssuedCoupon(user.getId(), coupon.getId())){
            throw new IllegalArgumentException("이미 발급된 쿠폰입니다.");
        }

        UserCoupon userCoupon = coupon.issue(user, LocalDate.now());

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
