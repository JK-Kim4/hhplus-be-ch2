package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.domain.salesStat.TypedScore;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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

    @Getter
    public static class Coupon {

        Long couponId;
        String couponName;
        String discountPolicy;
        BigDecimal discountAmount;
        LocalDate expiredAt;

        public static Coupon from(kr.hhplus.be.server.domain.coupon.Coupon coupon) {
            return Coupon.builder()
                    .couponId(coupon.getId())
                    .couponName(coupon.getName())
                    .discountPolicy(coupon.getDiscountPolicy().name())
                    .discountAmount(coupon.getDiscountAmount())
                    .expiredAt(coupon.getExpireDate())
                    .build();
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

    @Getter
    public static class Coupons {
        List<Coupon> coupons;

        public static Coupons from(List<kr.hhplus.be.server.domain.coupon.Coupon> coupons){
            return Coupons.builder()
                    .coupons(coupons.stream().map(Coupon::from).toList())
                    .build();
        }


        @Builder
        private Coupons(List<Coupon> coupons) {
            this.coupons = coupons;
        }

    }

    @Getter
    public static class RequestIssue {
        Long requestTimeMillis;
        Long userId;
        Long couponId;

        public static RequestIssue of(Long requestTimeMillis, Long userId, Long couponId){
            return RequestIssue.builder().requestTimeMillis(requestTimeMillis).userId(userId).couponId(couponId).build();
        }

        @Builder
        private RequestIssue(Long requestTimeMillis, Long userId, Long couponId) {
            this.requestTimeMillis = requestTimeMillis;
            this.userId = userId;
            this.couponId = couponId;
        }

    }

    @Getter
    public static class AvailableCouponIds {
        List<Long> couponIds;

        public static AvailableCouponIds of(List<Long> couponIds){
            return AvailableCouponIds.builder().couponIds(couponIds).build();
        }

        @Builder
        private AvailableCouponIds(List<Long> couponIds) {
            this.couponIds = couponIds;
        }
    }

    @Getter
    public static class FetchFromRedis {
        List<TypedScore> applicants;

        public static FetchFromRedis of(List<TypedScore> applicants){
            return FetchFromRedis.builder().applicants(applicants).build();
        }

        @Builder
        private FetchFromRedis(List<TypedScore> applicants) {
            this.applicants = applicants;
        }
    }

    @Getter
    public static class ExpiredCouponIds {
        List<Long> couponIds;

        public static ExpiredCouponIds of(List<Long> couponIds){
            return ExpiredCouponIds.builder().couponIds(couponIds).build();
        }

        @Builder
        private ExpiredCouponIds(List<Long> couponIds) {
            this.couponIds = couponIds;
        }
    }

    @Getter
    public static class RegisterApplicant {

    }
}
