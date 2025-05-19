package kr.hhplus.be.server.domain.coupon.couponApplicant;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public class CouponApplicantInfo {

    @Getter
    public static class RegisterApplicant{
        Long couponId;
        Long userId;
        LocalDateTime appliedAt;

        public static RegisterApplicant of(Long couponId, Long userId, LocalDateTime appliedAt) {
            return new RegisterApplicant(couponId, userId, appliedAt);
        }

        @Builder
        private RegisterApplicant(Long couponId, Long userId, LocalDateTime appliedAt) {
            this.couponId = couponId;
            this.userId = userId;
            this.appliedAt = appliedAt;
        }
    }

    @Getter
    public static class IssuableCoupons {
        List<Long> issuableCouponIds;

        public static IssuableCoupons of(List<Long> issuableCouponIds) {
            return IssuableCoupons.builder().issuableCouponIds(issuableCouponIds).build();
        }


        @Builder
        private IssuableCoupons(List<Long> issuableCouponIds) {
            this.issuableCouponIds = issuableCouponIds;
        }
    }

    @Getter
    public static class Applicants {
        List<Long> userIds;

        public static Applicants of(List<Long> userIds) {
            return Applicants.builder().userIds(userIds).build();
        }

        @Builder
        private Applicants(List<Long> userIds) {
            this.userIds = userIds;
        }

    }
}
