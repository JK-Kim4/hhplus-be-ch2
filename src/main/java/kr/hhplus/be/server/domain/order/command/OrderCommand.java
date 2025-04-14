package kr.hhplus.be.server.domain.order.command;

import kr.hhplus.be.server.domain.order.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderCommand {


    public static class Create {
        private Long userId;
        private Long userCouponId;
        private List<OrderItem> orderItems = new ArrayList<>();

        public static Create of(Long userId, Long userCouponId, List<OrderItem> orderItemCommands) {
            return new Create(userId, userCouponId, orderItemCommands);
        }

        public Order toEntity(List<kr.hhplus.be.server.domain.order.OrderItem> orderItems){
            Order order = Order.of(this.userId, this.userCouponId, orderItems);
            order.calculateTotalPrice();
            return order;
        }

        private Create(Long userId, Long userCouponId, List<OrderItem> orderItems) {
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

        public List<OrderItem> getOrderItems() {
            return orderItems;
        }
    }

    public static class OrderItem{

        private Long itemId;
        private Integer price;
        private Integer quantity;

        public static OrderItem of(Long itemId, Integer price, Integer quantity) {
            return new OrderItem(itemId, price, quantity);
        }

        public kr.hhplus.be.server.domain.order.OrderItem toEntity(){
            return new kr.hhplus.be.server.domain.order.OrderItem(itemId, price, quantity);
        }


        public OrderItem(Long itemId, Integer price, Integer quantity) {
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
