package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.domain.user.userCoupon.UserCouponCriteria;
import kr.hhplus.be.server.domain.user.userCoupon.UserCouponInfo;
import kr.hhplus.be.server.domain.user.userCoupon.UserCouponService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@Transactional
public class CouponFacade {

    private final UserCouponService userCouponService;
    private final CouponService couponService;
    private final UserService userService;

    public CouponFacade(
            UserCouponService userCouponService,
            CouponService couponService,
            UserService userService) {
        this.userCouponService = userCouponService;
        this.couponService = couponService;
        this.userService = userService;
    }

    public UserCouponInfo.Issue issue(UserCouponCriteria.Issue criteria) {
        Coupon coupon = couponService.findById(criteria.getCouponId());
        User user = userService.findById(criteria.getUserId());

        if(couponService.isAlreadyIssuedCoupon(user.getId(), coupon.getId())){
            throw new IllegalArgumentException("이미 발급된 쿠폰입니다.");
        }

        UserCoupon userCoupon = coupon.issueUserCoupon(user, LocalDate.now());

        couponService.saveUserCoupon(userCoupon);

        return UserCouponInfo.Issue.of(
                userCoupon.getId(),
                userCoupon.getCoupon().getId(),
                userCoupon.getUser().getId(),
                userCoupon.getIssueDateTime());
    }

    @Transactional(readOnly = true)
    public UserCouponInfo.UserCouponList findByUserId(Long userId) {
        List<kr.hhplus.be.server.domain.user.userCoupon.UserCoupon> couponList = userCouponService.findByUserId(userId);
        return UserCouponInfo.UserCouponList.of(couponList);
    }

}
