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

        @Builder
        private Items(Long productId, BigDecimal price, Integer quantity) {
            this.productId = productId;
            this.price = price;
            this.quantity = quantity;
        }

    }
}
