package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.userCoupon.UserCoupon;
import kr.hhplus.be.server.domain.userCoupon.UserCouponIssueCommand;
import kr.hhplus.be.server.domain.userCoupon.UserCouponService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CouponFacade {

    private final CouponService couponService;
    private final UserCouponService userCouponService;

    public CouponFacade(
            CouponService couponService,
            UserCouponService userCouponService) {
        this.couponService = couponService;
        this.userCouponService = userCouponService;
    }

    @Transactional(readOnly = false)
    public UserCouponIssueCommand.Response issue(UserCouponIssueCommand command) {
        return userCouponService.issue(command);
    }


    public List<UserCoupon> findByUserId(Long userId) {
        return userCouponService.findByUserId(userId);
    }
}
