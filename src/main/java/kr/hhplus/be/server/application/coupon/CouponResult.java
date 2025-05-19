package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.domain.coupon.CouponInfo;
import kr.hhplus.be.server.domain.coupon.couponApplicant.CouponApplicantInfo;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CouponResult {

    @Getter
    public static class RequestIssue {
        Long requestTimeMillis;
        Long userId;
        Long couponId;

        public static RequestIssue from(CouponInfo.RequestIssue info) {
            return RequestIssue.builder().requestTimeMillis(info.getRequestTimeMillis()).userId(info.getUserId()).couponId(info.getCouponId()).build();
        }

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
}
