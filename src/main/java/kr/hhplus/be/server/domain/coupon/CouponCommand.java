package kr.hhplus.be.server.domain.coupon;

import lombok.Builder;
import lombok.Getter;

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
}
