package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.domain.coupon.CouponInfo;
import kr.hhplus.be.server.domain.coupon.couponApplicant.CouponApplicantInfo;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class CouponResult {

    @Getter
    public static class Issue {

        Long couponId;
        Long userCouponId;
        LocalDateTime issuedAt;
        LocalDate expiredAt;

        public static CouponResult.Issue from(CouponInfo.Issue info) {
            return CouponResult.Issue.builder()
                    .couponId(info.getCouponId())
                    .userCouponId(info.getUserCouponId())
                    .issuedAt(info.getIssuedAt())
                    .expiredAt(info.getExpiredAt())
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
    public static class RequestRegister {
        Long userId;
        Long couponId;
        LocalDateTime appliedAt;


        public static RequestRegister from(CouponApplicantInfo.RegisterApplicant registerApplicant) {
            return RequestRegister.builder()
                    .userId(registerApplicant.getUserId())
                    .couponId(registerApplicant.getCouponId())
                    .appliedAt(registerApplicant.getAppliedAt())
                    .build();
        }

        @Builder
        private RequestRegister(Long userId, Long couponId, LocalDateTime appliedAt) {
            this.userId = userId;
            this.couponId = couponId;
            this.appliedAt = appliedAt;
        }
    }

    @Getter
    public static class UserCoupons{
        List<UserCoupon> userCoupons;

        public static UserCoupons from(CouponInfo.UserCoupons userCoupons) {
            return UserCoupons.builder()
                    .userCoupons(userCoupons.getUserCoupons().stream().map(UserCoupon::from).toList())
                    .build();
        }

        @Builder
        private UserCoupons(List<UserCoupon> userCoupons) {
            this.userCoupons = userCoupons;
        }
    }

    @Getter
    public static class UserCoupon {

        Long userCouponId;
        Long couponId;
        Long userId;
        String couponName;
        String discountPolicy;
        BigDecimal discountRate;
        LocalDate expiredAt;
        LocalDateTime issuedAt;
        LocalDateTime usedAt;

        public static UserCoupon from(CouponInfo.UserCoupon userCoupon) {
            return UserCoupon.builder()
                    .userCouponId(userCoupon.getCouponId())
                    .couponId(userCoupon.getCouponId())
                    .userId(userCoupon.getUserId())
                    .couponName(userCoupon.getCouponName())
                    .discountPolicy(userCoupon.getDiscountPolicy())
                    .discountRate(userCoupon.getDiscountRate())
                    .expiredAt(userCoupon.getExpiredAt())
                    .issuedAt(userCoupon.getIssuedAt())
                    .usedAt(userCoupon.getUsedAt())
                    .build();

        }

        @Builder
        private UserCoupon(Long userCouponId, Long couponId, Long userId, String couponName, String discountPolicy, BigDecimal discountRate, LocalDate expiredAt, LocalDateTime issuedAt, LocalDateTime usedAt) {
            this.userCouponId = userCouponId;
            this.couponId = couponId;
            this.userId = userId;
            this.couponName = couponName;
            this.discountPolicy = discountPolicy;
            this.discountRate = discountRate;
            this.expiredAt = expiredAt;
            this.issuedAt = issuedAt;
        }
    }
}
