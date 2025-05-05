package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.item.ItemCommand;
import kr.hhplus.be.server.domain.item.ItemInfo;
import kr.hhplus.be.server.domain.order.OrderCommand;
import lombok.Getter;

import java.util.List;

public class OrderCriteria {

    @Getter
    public static class Create {
        Long userId;
        Long userCouponId;
        List<Item> items;
        List<ItemInfo.OrderItem> orderItems;

        public static Create of(Long userId, Long userCouponId, List<Item> items) {
            return new Create(userId, userCouponId, items);
        }

        private Create(Long userId, Long userCouponId, List<Item> items) {
            this.userId = userId;
            this.userCouponId = userCouponId;
            this.items = items;
        }

        public List<ItemCommand.OrderItem> toOrderItemCommand() {
            return items.stream()
                    .map((item) -> ItemCommand.OrderItem.of(item.getItemId(), item.getPrice(), item.getQuantity()))
                    .toList();
        }

        public OrderCommand.CreateV2 toOrderCommand(List<ItemInfo.OrderItem> orderItems) {
            return OrderCommand.CreateV2.of(userId, userCouponId, orderItems);
        }
    }

    @Getter
    public static class Item {
        Long itemId;
        Integer price;
        Integer quantity;

        public static Item of(Long itemId, Integer price, Integer quantity) {
            return new Item(itemId, price, quantity);
        }

        private Item(Long itemId, Integer price, Integer quantity) {
            this.itemId = itemId;
            this.price = price;
            this.quantity = quantity;
        }
    }
}
