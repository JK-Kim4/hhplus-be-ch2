package kr.hhplus.be.server.interfaces.coupon.api;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import kr.hhplus.be.server.application.coupon.CouponCriteria;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class CouponRequest {

    @Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Issue implements Serializable {

        Long couponId;
        Long userId;

        public static Issue of(Long couponId, Long userId) {
            return Issue.builder().couponId(couponId).userId(userId).build();
        }

        @Builder
        private Issue(Long couponId, Long userId) {
            this.couponId = couponId;
            this.userId = userId;
        }

        public CouponCriteria.Issue toCriteria() {
            return CouponCriteria.Issue.of(couponId, userId);
        }

        public CouponCriteria.RequestRegister toRegisterCriteria() {
            return CouponCriteria.RequestRegister.builder()
                    .couponId(couponId)
                    .userId(userId)
                    .build();
        }
    }

    @Getter
    public static class UserCoupon {
        Long userId;

        @Builder
        private UserCoupon(Long userId) {
            this.userId = userId;
        }

        public CouponCriteria.UserCoupon toCriteria() {
            return CouponCriteria.UserCoupon.of(userId);
        }
    }

    @Getter
    public static class Create {

        String name;
        @NonNull @Min(1)
        Integer quantity;
        @NotBlank @Pattern(regexp = "FLAT|RATE")
        String discountPolicy;
        @NonNull
        BigDecimal discountAmount; // 정액이면 금액, 정률이면 비율(%)
        @Future(message = "만료일은 오늘 이후여야 합니다.")
        LocalDate expireDate;


        public CouponCriteria.Create toCriteria() {
            return CouponCriteria.Create.builder()
                    .name(name)
                    .quantity(quantity)
                    .discountPolicy(discountPolicy)
                    .discountAmount(discountAmount)
                    .expireDate(expireDate)
                    .build();
        }

        @Builder
        private Create(String name, Integer quantity, String discountPolicy, BigDecimal discountAmount, LocalDate expireDate) {
            this.name = name;
            this.quantity = quantity;
            this.discountPolicy = discountPolicy;
            this.discountAmount = discountAmount;
            this.expireDate = expireDate;
        }
    }
}
