package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.domain.coupon.CouponCommand;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CouponCriteria {

    @Getter
    public static class Issue{
        Long couponId;
        Long userId;

        public static Issue of(Long couponId, Long userId){
            return Issue.builder().couponId(couponId).userId(userId).build();
        }

        @Builder
        private Issue(Long couponId, Long userId) {
            this.couponId = couponId;
            this.userId = userId;
        }

        public CouponCommand.Issue toCommand() {
            return CouponCommand.Issue.builder()
                    .couponId(couponId)
                    .userId(userId)
                    .build();
        }
    }

    @Getter
    public static class RequestRegister {
        Long couponId;
        Long userId;

        @Builder
        private RequestRegister(Long couponId, Long userId) {
            this.couponId = couponId;
            this.userId = userId;
        }

        public CouponCommand.Issue toCommand() {
            return CouponCommand.Issue.builder()
                    .couponId(couponId)
                    .userId(userId)
                    .build();
        }

        public CouponCommand.RegisterApplicant toRegisterCommand() {
            return CouponCommand.RegisterApplicant.of(couponId, userId);
        }
    }

    @Getter
    public static class UserCoupon {
        Long userId;

        public static UserCoupon of(Long userId){
            return UserCoupon.builder().userId(userId).build();
        }

        @Builder
        private UserCoupon(Long userId) {
            this.userId = userId;
        }
    }

    @Getter
    public static class Create {
        String name;
        Integer quantity;
        String discountPolicy;
        BigDecimal discountAmount;
        LocalDate expireDate;

        public CouponCommand.Create toCommand() {
            return CouponCommand.Create.of(name, quantity, discountPolicy, discountAmount, expireDate);
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
