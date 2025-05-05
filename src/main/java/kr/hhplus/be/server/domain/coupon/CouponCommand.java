package kr.hhplus.be.server.domain.coupon;

import lombok.Builder;
import lombok.Getter;

public class CouponCommand {

    @Getter
    public static class Issue {
        Long couponId;
        Long userId;

        @Builder
        private Issue(Long couponId, Long userId) {
            this.couponId = couponId;
            this.userId = userId;
        }
    }
}
