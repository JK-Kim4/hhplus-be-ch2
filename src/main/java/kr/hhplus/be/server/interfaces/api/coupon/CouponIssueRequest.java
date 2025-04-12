package kr.hhplus.be.server.interfaces.api.coupon;

import kr.hhplus.be.server.domain.userCoupon.UserCouponIssueCommand;

public record CouponIssueRequest(
        Long userId
) {

    public UserCouponIssueCommand toUserCouponIssueCommand(Long couponId){
        return new UserCouponIssueCommand(couponId, this.userId);
}

}
