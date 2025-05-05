package kr.hhplus.be.server.domain.item;

import lombok.Getter;

public class ItemInfo {

    @Getter
    public static class OrderItem {
        Long itemId;
        Integer price;
        Integer quantity;

        public static OrderItem from(Item item) {
            return new OrderItem(item.getId(), item.price(), item.stock());
        }

        public static OrderItem of(Long itemId, Integer price, Integer quantity) {
            return new OrderItem(itemId, price, quantity);
        }

        private OrderItem(Long itemId, Integer price, Integer quantity) {
            this.itemId = itemId;
            this.price = price;
            this.quantity = quantity;
        }

    }
}
