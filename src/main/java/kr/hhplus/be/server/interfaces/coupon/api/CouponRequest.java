package kr.hhplus.be.server.interfaces.coupon.api;

import kr.hhplus.be.server.application.coupon.CouponCriteria;
import lombok.Builder;
import lombok.Getter;

public class CouponRequest {

    @Getter
    public static class Issue {

        Long couponId;
        Long userId;

        public static Issue of(Long couponId, Long userId) {
            return Issue.builder().couponId(couponId).userId(userId).build();
        }

        @Builder
        private Issue(Long couponId, Long userId) {
            this.couponId = couponId;
            this.userId = userId;
        }

        public CouponCriteria.Issue toCriteria() {
            return CouponCriteria.Issue.of(couponId, userId);
        }

        public CouponCriteria.RequestRegister toRegisterCriteria() {
            return CouponCriteria.RequestRegister.builder()
                    .couponId(couponId)
                    .userId(userId)
                    .build();
        }
    }
}
