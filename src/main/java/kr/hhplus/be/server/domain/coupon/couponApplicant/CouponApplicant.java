package kr.hhplus.be.server.domain.coupon.couponApplicant;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter @NoArgsConstructor
public class CouponApplicant {

    private Long userId;
    private Long couponId;
    private LocalDateTime appliedAt;
    private LocalDateTime issuedAt;

    public static CouponApplicant of(Long userId, Long couponId, Long appliedAt) {
        return CouponApplicant.builder()
                .userId(userId)
                .couponId(couponId)
                .appliedAt(appliedAt)
                .build();
    }

    public void markAsIssued(LocalDateTime issuedAt){
        this.issuedAt = issuedAt;
    }

    @Builder
    private CouponApplicant(Long userId, Long couponId, Long appliedAt) {
        this.userId = userId;
        this.couponId = couponId;
        this.appliedAt = Instant.ofEpochMilli(appliedAt)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}
