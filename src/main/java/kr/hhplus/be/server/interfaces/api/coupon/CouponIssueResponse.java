package kr.hhplus.be.server.interfaces.api.coupon;

import kr.hhplus.be.server.domain.userCoupon.UserCouponIssueCommand;

public record CouponIssueResponse(Long userCouponId, Long couponId, Long userId) {



    public static CouponIssueResponse from(UserCouponIssueCommand.Response response) {
        return new CouponIssueResponse(response.getUserCoupon().getId(), response.getUserCoupon().getCoupon().getId(), response.getUserCoupon().getUser().getId());
    }

}
