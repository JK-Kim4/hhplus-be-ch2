package kr.hhplus.be.server.controller.coupon;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(name = "CouponIssueResponse: 쿠폰 발급 응답")
public class CouponIssueResponse {

    @Schema(name = "couponId", description = "쿠폰 고유 번호", example = "1")
    private Long couponId;

    @Schema(name = "userId", description = "사용자 고유 번호", example = "10")
    private Long userId;

    @Schema(name = "userPointId", description = "발급된 사용자 쿠폰 고유번호", example = "52")
    private Long userPointId;

    @Schema(name = "issueDateTime", description = "발급 일시", example = "2025-01-01H00:00:00")
    private LocalDateTime issueDateTime;

    public CouponIssueResponse() {}

    public CouponIssueResponse(
            Long couponId, Long userId,
            Long userPointId, LocalDateTime issueDateTime) {
        this.couponId = couponId;
        this.userId = userId;
        this.userPointId = userPointId;
        this.issueDateTime = issueDateTime;
    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserPointId() {
        return userPointId;
    }

    public void setUserPointId(Long userPointId) {
        this.userPointId = userPointId;
    }

    public LocalDateTime getIssueDateTime() {
        return issueDateTime;
    }

    public void setIssueDateTime(LocalDateTime issueDateTime) {
        this.issueDateTime = issueDateTime;
    }
}
