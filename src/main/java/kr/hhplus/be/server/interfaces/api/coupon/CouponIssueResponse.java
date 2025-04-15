package kr.hhplus.be.server.interfaces.api.coupon;

import kr.hhplus.be.server.domain.userCoupon.UserCouponInfo;

public record CouponIssueResponse(
        Long userCouponId,
        Long couponId,
        Long userId) {

    public static CouponIssueResponse from(UserCouponInfo.Issue info) {
        return new CouponIssueResponse(
                info.getUserCouponId(),
                info.getCouponId(),
                info.getUserId());
    }



}
