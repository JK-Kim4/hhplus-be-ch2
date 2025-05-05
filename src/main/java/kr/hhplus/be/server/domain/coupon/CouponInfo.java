package kr.hhplus.be.server.domain.coupon;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CouponInfo {

    @Getter
    public static class Issue {
        Long couponId;
        Long userCouponId;
        LocalDateTime issuedAt;
        LocalDate expiredAt;

        public static Issue from(UserCoupon userCoupon) {
            return Issue.builder()
                    .couponId(userCoupon.getCoupon().getId())
                    .userCouponId(userCoupon.getId())
                    .issuedAt(userCoupon.getIssuedAt())
                    .expiredAt(userCoupon.getCoupon().getExpireDate())
                    .build();
        }

        @Builder
        private Issue(Long couponId, Long userCouponId, LocalDateTime issuedAt, LocalDate expiredAt) {
            this.couponId = couponId;
            this.userCouponId = userCouponId;
            this.issuedAt = issuedAt;
            this.expiredAt = expiredAt;
        }

    }
}
