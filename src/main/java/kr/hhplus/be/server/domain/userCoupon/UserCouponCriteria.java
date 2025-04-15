package kr.hhplus.be.server.domain.userCoupon;

import lombok.Getter;

public class UserCouponCriteria {

    @Getter
    public static class Issue{

        private Long couponId;
        private Long userId;

        public UserCouponIssueCommand toCommand() {
            return UserCouponIssueCommand.of(couponId, userId);
        }

        public Issue(Long couponId, Long userId) {
            this.couponId = couponId;
            this.userId = userId;
        }
    }
}
