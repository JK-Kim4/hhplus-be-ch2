package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.domain.coupon.CouponInfo;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
}
