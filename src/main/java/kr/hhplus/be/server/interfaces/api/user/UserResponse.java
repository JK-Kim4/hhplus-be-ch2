package kr.hhplus.be.server.interfaces.api.user;

import kr.hhplus.be.server.application.user.UserResult;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class UserResponse {

    @Getter
    public static class Create {

        Long userId;
        String username;
        BigDecimal point;

        public static Create from(UserResult.Create user) {
            return new Create(user.getUserId(), user.getUsername(), user.getPoint());
        }

        @Builder
        private Create(Long userId, String username, BigDecimal point) {
            this.userId = userId;
            this.username = username;
            this.point = point;
        }
    }

    @Getter
    public static class Coupons {
        List<UserResponse.Coupon> coupons;

        public static Coupons from(List<UserResult.Coupon> coupons) {
            return Coupons.builder()
                    .coupons(coupons.stream().map((coupon) ->
                        UserResponse.Coupon.of(
                                coupon.getCouponId(),
                                coupon.getCouponName(),
                                coupon.getDiscountPolicy(),
                                coupon.getDiscountAmount(),
                                coupon.getExpiredAt())
                    ).toList())
                    .build();
        }

        @Builder
        private Coupons(List<UserResponse.Coupon> coupons) {
            this.coupons = coupons;
        }
    }


    @Getter
    public static class Coupon {
        Long couponId;
        String couponName;
        String discountPolicy;
        BigDecimal discountRate;
        LocalDate expiredAt;

        public static UserResponse.Coupon of(Long couponId, String couponName, String discountPolicy, BigDecimal discountRate, LocalDate expiredAt){
            return Coupon.builder().couponId(couponId).couponName(couponName).discountPolicy(discountPolicy).discountRate(discountRate).expiredAt(expiredAt).build();
        }


        @Builder
        private Coupon(Long couponId, String couponName, String discountPolicy, BigDecimal discountRate, LocalDate expiredAt) {
            this.couponId = couponId;
            this.couponName = couponName;
            this.discountPolicy = discountPolicy;
            this.discountRate = discountRate;
            this.expiredAt = expiredAt;
        }
    }
}
