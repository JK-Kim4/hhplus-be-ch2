package kr.hhplus.be.server.domain.coupon;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CouponCommand {

    @Getter
    public static class Issue {
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
    }

    @Getter
    public static class FetchFromRedis {
        Long couponId;
        Integer quantity;

        public static FetchFromRedis of(Long couponId, Integer quantity){
            return FetchFromRedis.builder().couponId(couponId).quantity(quantity).build();
        }

        @Builder
        private FetchFromRedis(Long couponId, Integer quantity) {
            this.couponId = couponId;
            this.quantity = quantity;
        }
    }

    @Getter
    public static class InitCoupon {
        Long couponId;
        Integer quantity;
        Long expiredMillis;

        public static InitCoupon of(Long couponId, Integer quantity, Long expiredMillis){
            return InitCoupon.builder().couponId(couponId).quantity(quantity).expiredMillis(expiredMillis).build();
        }

        @Builder
        private InitCoupon(Long couponId, Integer quantity, Long expiredMillis) {
            this.couponId = couponId;
            this.quantity = quantity;
            this.expiredMillis = expiredMillis;
        }

    }

    @Getter
    public static class DeleteKey {
        Long couponId;

        public static DeleteKey of(Long couponId){
            return DeleteKey.builder().couponId(couponId).build();
        }

        @Builder
        private DeleteKey(Long couponId) {
            this.couponId = couponId;
        }
    }

    @Getter
    public static class RegisterApplicant {
        Long couponId;
        Long userId;

        public static RegisterApplicant of(Long couponId, Long userId){
            return RegisterApplicant.builder().couponId(couponId).userId(userId).build();
        }

        @Builder
        private RegisterApplicant(Long couponId, Long userId) {
            this.couponId = couponId;
            this.userId = userId;
        }
    }

    @Getter
    public static class UserCoupon {
        Long userId;


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

        public static Create of(String name, Integer quantity, String discountPolicy, BigDecimal discountAmount, LocalDate expireDate){
            return Create.builder()
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
