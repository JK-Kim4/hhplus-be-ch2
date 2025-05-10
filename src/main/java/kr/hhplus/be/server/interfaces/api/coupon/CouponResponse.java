package kr.hhplus.be.server.interfaces.api.coupon;

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
}
