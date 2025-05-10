package kr.hhplus.be.server.interfaces.api.order;

import kr.hhplus.be.server.application.order.OrderCriteria;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderRequest {

    @Getter
    public static class Order {

        Long userId;
        Long userCouponId;
        List<Item> items = new ArrayList<>();

        public static Order of(Long userId, Long userCouponId, List<Item> items){
            return Order.builder()
                        .userId(userId)
                        .userCouponId(userCouponId)
                        .items(items)
                    .build();
        }

        public OrderCriteria.Create toCriteria() {
            return OrderCriteria.Create.of(
                    this.userId,
                    this.userCouponId,
                    Item.toCriteriaList(this.items)
            );
        }

        @Builder
        private Order(Long userId, Long userCouponId, List<Item> items) {
            this.userId = userId;
            this.userCouponId = userCouponId;
            this.items = items;
        }
    }

    @Getter
    public static class Item {

        Long productId;
        BigDecimal price;
        Integer quantity;

        public static Item of(Long productId, BigDecimal price, Integer quantity){
            return Item.builder()
                        .productId(productId)
                        .price(price)
                        .quantity(quantity)
                    .build();
        }

        public static List<OrderCriteria.Items> toCriteriaList(List<Item> items){
            return items.stream().map((i) -> OrderCriteria.Items.builder()
                        .productId(i.getProductId())
                        .price(i.getPrice())
                        .quantity(i.getQuantity())
                    .build()
            ).toList();
        }

        @Builder
        private Item(Long productId, BigDecimal price, Integer quantity) {
            this.productId = productId;
            this.price = price;
            this.quantity = quantity;
        }

    }
}
