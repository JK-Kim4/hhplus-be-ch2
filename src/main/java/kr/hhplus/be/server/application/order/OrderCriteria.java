package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.order.OrderCommand;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderCriteria {

    @Getter
    public static class Create {

        Long userId;
        Long userCouponId;
        List<Items> items = new ArrayList<>();

        public static OrderCommand.Create toCommand(Create create){
            return OrderCommand.Create.of(create.getUserId(), create.getUserCouponId(), Items.toCommandList(create.getItems()));
        }

        public static OrderCommand.ApplyCoupon toApplyCouponCommand(Create create){
            return OrderCommand.ApplyCoupon.of(create.getUserId(), create.getUserCouponId());
        }

        public static OrderCriteria.Create of(Long userId, Long userCouponId, List<Items> items){
            return OrderCriteria.Create.builder().userId(userId).userCouponId(userCouponId).items(items).build();
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

        public static List<OrderCommand.Items> toCommandList(List<Items> items){
            return items.stream().map((i) -> OrderCommand.Items.builder()
                    .productId(i.getProductId())
                    .price(i.getPrice())
                    .quantity(i.getQuantity())
                    .build()
            ).toList();
        }

        @Builder
        private Items(Long productId, BigDecimal price, Integer quantity) {
            this.productId = productId;
            this.price = price;
            this.quantity = quantity;
        }
    }
}
