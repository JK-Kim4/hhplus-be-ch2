package kr.hhplus.be.server.interfaces.coupon.api;

import kr.hhplus.be.server.application.coupon.CouponResult;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class CouponResponse {


    @Getter
    public static class UserCoupons{
        List<UserCoupon> userCoupons;

        public static UserCoupons from(CouponResult.UserCoupons userCoupons){
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

        public static UserCoupon from(CouponResult.UserCoupon userCoupon){
            return UserCoupon.builder()
                    .userCouponId(userCoupon.getUserCouponId())
                    .couponId(userCoupon.getCouponId())
                    .userId(userCoupon.getUserId())
                    .couponName(userCoupon.getCouponName())
                    .discountPolicy(userCoupon.getDiscountPolicy())
                    .discountRate(userCoupon.getDiscountRate())
                    .expiredAt(userCoupon.getExpiredAt())
                    .usedAt(userCoupon.getUsedAt())
                    .issuedAt(userCoupon.getIssuedAt())
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

    @Getter
    public static class Issue {
        Long couponId;
        Long userCouponId;
        LocalDateTime issuedAt;
        LocalDate expiredAt;

        public static Issue from(CouponResult.Issue issue) {
            return Issue.builder()
                        .couponId(issue.getCouponId())
                        .userCouponId(issue.getUserCouponId())
                        .issuedAt(issue.getIssuedAt())
                        .expiredAt(issue.getExpiredAt())
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
    public static class RequestRegister{
        Long userId;
        Long couponId;
        LocalDateTime appliedAt;

        public static RequestRegister from(CouponResult.RequestRegister register){
            return RequestRegister.builder()
                    .userId(register.getUserId())
                    .couponId(register.getCouponId())
                    .appliedAt(register.getAppliedAt())
                    .build();
        }


        @Builder
        private RequestRegister(Long userId, Long couponId, LocalDateTime appliedAt) {
            this.userId = userId;
            this.couponId = couponId;
            this.appliedAt = appliedAt;
        }


    }
}
