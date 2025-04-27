package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.item.Item;

import java.util.ArrayList;
import java.util.List;

public class OrderCommand {


    public static class Create {
        private Long userId;
        private Long userCouponId;
        private List<OrderItemCreate> orderItems = new ArrayList<>();

        public static Create of(Long userId, Long userCouponId, List<OrderItemCreate> orderItemCommands) {
            return new Create(userId, userCouponId, orderItemCommands);
        }

        private Create(Long userId, Long userCouponId, List<OrderItemCreate> orderItems) {
            this.userId = userId;
            this.userCouponId = userCouponId;
            this.orderItems = orderItems;
        }

        public Long getUserId() {
            return userId;
        }

        public Long getUserCouponId() {
            return userCouponId;
        }

        public List<OrderItemCreate> getOrderItems() {
            return orderItems;
        }
    }

    public static class OrderItemCreate{

        private Long itemId;
        private Integer price;
        private Integer quantity;

        public OrderItem toEntity(Item item){
            return OrderItem.createWithItemAndPriceAndQuantity(item, price, quantity);
        }

        public OrderItemCreate(Long itemId, Integer price, Integer quantity) {
            this.itemId = itemId;
            this.price = price;
            this.quantity = quantity;
        }

        public Long getItemId() {
            return itemId;
        }

        public Integer getPrice() {
            return price;
        }

        public Integer getQuantity() {
            return quantity;
        }
    }


}
