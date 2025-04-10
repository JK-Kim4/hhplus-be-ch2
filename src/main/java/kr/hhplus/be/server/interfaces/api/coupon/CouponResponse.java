package kr.hhplus.be.server.interfaces.api.coupon;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.hhplus.be.server.domain.coupon.CouponType;

import java.time.LocalDateTime;

public class CouponResponse {

    static public class Create {
        @Schema(name = "couponId", description = "쿠폰 고유 번호", example = "1")
        private Long couponId;

        @Schema(name = "name", description = "쿠폰 이름", example = "전 상품 10% 할인 쿠폰")
        private String name;

        @Schema(name = "couponType", description = "쿠폰 타입(RATE: % 할인, PRICE: 정액 할인)", example = "RATE")
        private CouponType couponType;

        @Schema(name = "discountRate", description = "(RATE 쿠폰) 쿠폰 할인률", example = "15.0")
        private float discountRate;

        @Schema(name = "discountPrice", description = "(PRICE 쿠폰) 쿠폰 할인 금액", example = "50000")
        private Integer discountPrice;

        @Schema(name = "remainQuantity", description = "쿠폰 잔여 수량", example = "30")
        private Integer remainQuantity;

        @Schema(name = "expireDateTime", description = "쿠폰 만료 일시", example = "2025-01-01T00:00:00")
        private LocalDateTime expireDateTime;

        public Create() {}

        public Create(
                Long couponId, String name,
                CouponType couponType, float discountRate,
                Integer discountPrice, Integer remainQuantity,
                LocalDateTime expireDateTime) {
            this.couponId = couponId;
            this.name = name;
            this.couponType = couponType;
            this.discountRate = discountRate;
            this.discountPrice = discountPrice;
            this.remainQuantity = remainQuantity;
            this.expireDateTime = expireDateTime;
        }

        public Long getCouponId() {
            return couponId;
        }

        public void setCouponId(Long couponId) {
            this.couponId = couponId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public CouponType getCouponType() {
            return couponType;
        }

        public void setCouponType(CouponType couponType) {
            this.couponType = couponType;
        }

        public float getDiscountRate() {
            return discountRate;
        }

        public void setDiscountRate(float discountRate) {
            this.discountRate = discountRate;
        }

        public Integer getDiscountPrice() {
            return discountPrice;
        }

        public void setDiscountPrice(Integer discountPrice) {
            this.discountPrice = discountPrice;
        }

        public Integer getRemainQuantity() {
            return remainQuantity;
        }

        public void setRemainQuantity(Integer remainQuantity) {
            this.remainQuantity = remainQuantity;
        }

        public LocalDateTime getExpireDateTime() {
            return expireDateTime;
        }

        public void setExpireDateTime(LocalDateTime expireDateTime) {
            this.expireDateTime = expireDateTime;
        }
    }

    static public class Issue {

        @Schema(name = "couponId", description = "쿠폰 고유 번호", example = "1")
        private Long couponId;

        @Schema(name = "userId", description = "사용자 고유 번호", example = "10")
        private Long userId;

        @Schema(name = "userPointId", description = "발급된 사용자 쿠폰 고유번호", example = "52")
        private Long userPointId;

        @Schema(name = "issueDateTime", description = "발급 일시", example = "2025-01-01H00:00:00")
        private LocalDateTime issueDateTime;

        public Issue() {
        }

        public Issue(
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
}
