package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.domain.userCoupon.UserCoupon;
import kr.hhplus.be.server.domain.userCoupon.UserCouponCriteria;
import kr.hhplus.be.server.domain.userCoupon.UserCouponInfo;
import kr.hhplus.be.server.domain.userCoupon.UserCouponService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponFacade {

    private final UserCouponService userCouponService;

    public CouponFacade(UserCouponService userCouponService) {
        this.userCouponService = userCouponService;
    }

    public UserCouponInfo.Issue issue(UserCouponCriteria.Issue criteria) {
        UserCoupon issue = userCouponService.issue(criteria.toCommand());
        return UserCouponInfo.Issue.of(
                issue.getId(),
                issue.getCoupon().getId(),
                issue.getUser().getId(),
                issue.getIssueDateTime());
    }

    public UserCouponInfo.UserCouponList findByUserId(Long userId) {
        List<UserCoupon> couponList = userCouponService.findByUserId(userId);
        return UserCouponInfo.UserCouponList.of(couponList);
    }

}
