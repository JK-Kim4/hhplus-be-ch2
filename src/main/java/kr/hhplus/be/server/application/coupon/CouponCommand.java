package kr.hhplus.be.server.application.coupon;

import lombok.Getter;

public class CouponCommand {

    @Getter
    public static class Issue {

        private Long couponId;
        private Long userId;

        public static Issue of(Long couponId, Long userId) {
            return new Issue(couponId, userId);
        }

        private Issue(Long couponId, Long userId) {
            this.couponId = couponId;
            this.userId = userId;
        }
    }
}
