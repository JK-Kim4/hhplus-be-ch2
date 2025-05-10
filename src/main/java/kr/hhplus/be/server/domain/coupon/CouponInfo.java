package kr.hhplus.be.server.domain.coupon;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

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

    @Getter
    public static class Coupon {

        kr.hhplus.be.server.domain.coupon.Coupon coupon;

        @Builder
        private Coupon(kr.hhplus.be.server.domain.coupon.Coupon coupon) {
            this.coupon = coupon;
        }

        public static Coupon from(kr.hhplus.be.server.domain.coupon.Coupon coupon) {
            return Coupon.builder().coupon(coupon).build();
        }
    }

    @Getter
    public static class UserCouponOptional {
        Optional<UserCoupon> userCoupon;

        @Builder
        private UserCouponOptional(Optional<UserCoupon> userCoupon) {
            this.userCoupon = userCoupon;
        }

        public static UserCouponOptional from(Optional<UserCoupon> userCoupon) {
            return UserCouponOptional.builder().userCoupon(userCoupon).build();
        }
    }
}
