package kr.hhplus.be.server.interfaces.api.order;

import kr.hhplus.be.server.application.order.OrderCriteria;
import lombok.Getter;

import java.util.List;

public class OrderRequest {

    @Getter
    public static class Order {

        Long userId;
        Long userCouponId;
        List<Item> items;

        public Order(Long userId, Long userCouponId, List<Item> items) {
            this.userId = userId;
            this.userCouponId = userCouponId;
            this.items = items;
        }

        public OrderCriteria.Create toCriteria() {
            return OrderCriteria.Create.of(
                    this.userId,
                    this.userCouponId,
                    this.items.stream()
                            .map(item -> OrderCriteria.Item.of(item.getItemId(), item.getPrice(), item.getQuantity()))
                            .toList()
            );
        }
    }


    @Getter
    public static class Item {
        Long itemId;
        Integer price;
        Integer quantity;

        public Item(Long itemId, Integer price, Integer quantity) {
            this.itemId = itemId;
            this.price = price;
            this.quantity = quantity;
        }
    }
}
