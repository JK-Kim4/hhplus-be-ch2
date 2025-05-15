package kr.hhplus.be.server.application.user;

import kr.hhplus.be.server.domain.coupon.CouponInfo;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserResult {

    @Getter
    public static class Create {
        Long userId;
        String username;
        BigDecimal point;

        public static Create of(Long userId, String username, BigDecimal point){
            return Create.builder().userId(userId).username(username).point(point).build();
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
        List<UserResult.Coupon> coupons = new ArrayList<>();

        public static Coupons from(List<CouponInfo.Coupon> coupons) {
            return Coupons.builder()
                    .coupons(coupons.stream().map( coupon ->
                            UserResult.Coupon.of(
                                    coupon.getCouponId(),
                                    coupon.getCouponName(),
                                    coupon.getDiscountPolicy(),
                                    coupon.getDiscountAmount(),
                                    coupon.getExpiredAt()
                            )
                    ).toList())
                    .build();
        }

        public static Coupons of(List<UserResult.Coupon> coupons){
            return Coupons.builder().coupons(coupons).build();
        }

        @Builder
        private Coupons(List<UserResult.Coupon> coupons) {
            this.coupons = coupons;
        }
    }


    @Getter
    public static class Coupon {
        Long couponId;
        String couponName;
        String discountPolicy;
        BigDecimal discountAmount;
        LocalDate expiredAt;

        public static Coupon of(Long couponId, String couponName, String discountPolicy, BigDecimal discountAmount, LocalDate expiredAt){
            return Coupon.builder().couponId(couponId).couponName(couponName).discountPolicy(discountPolicy).discountAmount(discountAmount).expiredAt(expiredAt).build();
        }

        @Builder
        private Coupon(Long couponId, String couponName, String discountPolicy, BigDecimal discountAmount, LocalDate expiredAt) {
            this.couponId = couponId;
            this.couponName = couponName;
            this.discountPolicy = discountPolicy;
            this.discountAmount = discountAmount;
            this.expiredAt = expiredAt;
        }

    }
}
