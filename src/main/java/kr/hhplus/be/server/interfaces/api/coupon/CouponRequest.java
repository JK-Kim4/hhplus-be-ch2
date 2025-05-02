package kr.hhplus.be.server.interfaces.api.coupon;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.hhplus.be.server.application.coupon.CouponCommand;
import kr.hhplus.be.server.domain.coupon.CouponType;
import kr.hhplus.be.server.domain.coupon.userCoupon.UserCouponCriteria;
import lombok.Getter;

import java.time.LocalDateTime;

public class CouponRequest {

    @Getter
    public static class Create {

        @Schema(name = "name", description = "쿠폰 이름", example = "전 상품 10% 할인 쿠폰")
        private String name;

        @Schema(name="couponType", description = "쿠폰 타입(RATE: % 할인, PRICE: 정액 할인)", example = "RATE")
        private CouponType couponType;

        @Schema(name = "discountRate", description = "(RATE 쿠폰) 쿠폰 할인률", example = "15.0")
        private float discountRate;

        @Schema(name = "discountPrice", description = "(PRICE 쿠폰) 쿠폰 할인 금액", example = "50000")
        private Integer discountPrice;

        @Schema(name = "remainQuantity", description = "쿠폰 잔여 수량", example = "30")
        private Integer remainQuantity;

        @Schema(name = "expireDateTime", description = "쿠폰 만료 일시", example = "2025-01-01T00:00:00")
        private LocalDateTime expireDateTime;

        public Create(){}

        public Create(
                String name, CouponType couponType,
                float discountRate, Integer discountPrice,
                Integer remainQuantity, LocalDateTime expireDateTime) {
            this.name = name;
            this.couponType = couponType;
            this.discountRate = discountRate;
            this.discountPrice = discountPrice;
            this.remainQuantity = remainQuantity;
            this.expireDateTime = expireDateTime;
        }


    }

    @Getter
    public static class Issue {

        private Long userId;

        public Issue(Long userId) {
            this.userId = userId;
        }

        public CouponCommand.Issue toCommand(Long couponId){
            return CouponCommand.Issue.of(couponId, userId);
        }

        public UserCouponCriteria.Issue toCriteria(Long couponId){
            return new UserCouponCriteria.Issue(couponId, this.userId);
        }

    }
}
