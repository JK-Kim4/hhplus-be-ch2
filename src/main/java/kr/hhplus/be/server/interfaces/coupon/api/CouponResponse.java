package kr.hhplus.be.server.interfaces.coupon.api;

import kr.hhplus.be.server.application.coupon.CouponResult;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CouponResponse {


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
    public static class RequestIssue {

        Long requestTimeMillis;
        Long userId;
        Long couponId;

        public static RequestIssue from(CouponResult.RequestIssue info) {
            return RequestIssue.builder()
                    .couponId(info.getCouponId())
                    .userId(info.getUserId())
                    .requestTimeMillis(info.getRequestTimeMillis())
                    .build();
        }

        @Builder
        private RequestIssue(Long requestTimeMillis, Long userId, Long couponId) {
            this.requestTimeMillis = requestTimeMillis;
            this.userId = userId;
            this.couponId = couponId;
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
