package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.domain.user.userCoupon.UserCoupon;
import kr.hhplus.be.server.domain.user.userCoupon.UserCouponCriteria;
import kr.hhplus.be.server.domain.user.userCoupon.UserCouponInfo;
import kr.hhplus.be.server.domain.user.userCoupon.UserCouponService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
public class CouponFacade {

    private final UserCouponService userCouponService;
    private final CouponService couponService;
    private final UserService userService;

    public CouponFacade(
            UserCouponService userCouponService,
            CouponService couponService, UserService userService) {
        this.userCouponService = userCouponService;
        this.couponService = couponService;
        this.userService = userService;
    }

    public UserCouponInfo.Issue issue(UserCouponCriteria.Issue criteria) {
        //쿠폰 조회
        Coupon coupon = couponService.findById(criteria.getCouponId());
        //사용자 조회
        User user = userService.findById(criteria.getUserId());

        //쿠폰 생성
            //만료일 검증 / 잔여 수량 검증
            //사용자 동일 쿠폰 소지 여부
        UserCoupon userCoupon = coupon.issue(user);

        //쿠폰 저장
        userCouponService.save(userCoupon);
        user.addUserCoupon(userCoupon);

        return UserCouponInfo.Issue.of(
                userCoupon.getId(),
                userCoupon.getCoupon().getId(),
                userCoupon.getUser().getId(),
                userCoupon.getIssueDateTime());
    }

    @Transactional(readOnly = true)
    public UserCouponInfo.UserCouponList findByUserId(Long userId) {
        List<UserCoupon> couponList = userCouponService.findByUserId(userId);
        return UserCouponInfo.UserCouponList.of(couponList);
    }

}
