package kr.hhplus.be.server.domain.order;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderCommand {


    @Getter
    public static class Create {
        Long userId;
        Long userCouponId;
        List<Items> items = new ArrayList<>();
        List<OrderItem> orderItems = new ArrayList<>();

        public static Create of(Long userId, Long userCouponId, List<Items> items){
            return Create.builder().userId(userId).userCouponId(userCouponId).items(items).build();
        }

        @Builder
        private Create(Long userId, Long userCouponId, List<Items> items) {
            this.userId = userId;
            this.userCouponId = userCouponId;
            this.items = items;
        }
    }


    @Getter
    public static class Items {
        Long productId;
        BigDecimal price;
        Integer quantity;

        public static Items of(Long productId, BigDecimal price, Integer quantity){
            return Items.builder().productId(productId).price(price).quantity(quantity).build();
        }

        @Builder
        private Items(Long productId, BigDecimal price, Integer quantity) {
            this.productId = productId;
            this.price = price;
            this.quantity = quantity;
        }

    }

    @Getter
    public static class ApplyCoupon {
        Long orderId;
        Long userCouponId;

        public static ApplyCoupon of(Long orderId, Long userCouponId){
            return ApplyCoupon.builder().orderId(orderId).userCouponId(userCouponId).build();
        }

        @Builder
        private ApplyCoupon(Long orderId, Long userCouponId) {
            this.orderId = orderId;
            this.userCouponId = userCouponId;
        }

    }
}
