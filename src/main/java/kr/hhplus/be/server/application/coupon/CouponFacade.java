package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.domain.couponv2.CouponV2;
import kr.hhplus.be.server.domain.couponv2.CouponV2Service;
import kr.hhplus.be.server.domain.couponv2.UserCouponV2;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.domain.user.userCoupon.UserCoupon;
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
    private final CouponV2Service couponV2Service;
    private final UserService userService;

    public CouponFacade(
            UserCouponService userCouponService,
            CouponV2Service couponV2Service,
            UserService userService) {
        this.userCouponService = userCouponService;
        this.couponV2Service = couponV2Service;
        this.userService = userService;
    }

    public UserCouponInfo.Issue issue(UserCouponCriteria.Issue criteria) {
        CouponV2 couponV2 = couponV2Service.findById(criteria.getCouponId());
        User user = userService.findById(criteria.getUserId());

        if(couponV2Service.isAlreadyIssuedCoupon(user.getId(), couponV2.getId())){
            throw new IllegalArgumentException("이미 발급된 쿠폰입니다.");
        }

        UserCouponV2 userCouponV2 = couponV2.issueUserCoupon(user, LocalDate.now());

        couponV2Service.saveUserCoupon(userCouponV2);

        return UserCouponInfo.Issue.of(
                userCouponV2.getId(),
                userCouponV2.getCoupon().getId(),
                userCouponV2.getUser().getId(),
                userCouponV2.getIssueDateTime());
    }

    @Transactional(readOnly = true)
    public UserCouponInfo.UserCouponList findByUserId(Long userId) {
        List<UserCoupon> couponList = userCouponService.findByUserId(userId);
        return UserCouponInfo.UserCouponList.of(couponList);
    }

}
