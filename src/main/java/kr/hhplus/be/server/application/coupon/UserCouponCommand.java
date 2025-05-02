package kr.hhplus.be.server.application.coupon;

import lombok.Getter;

public class UserCouponCommand {

    @Getter
    public static class Issue {

        private Long couponId;
        private Long userId;

        public Issue(Long couponId, Long userId) {
            this.couponId = couponId;
            this.userId = userId;
        }


    }
}
