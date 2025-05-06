package kr.hhplus.be.server.domain.order;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderInfo {

    @Getter
    public static class Create {
        Long orderId;
        BigDecimal totalPrice;
        BigDecimal finalPrice;
        LocalDateTime orderedAt;

        public static Create from(Order order) {
            return new Create(order.getId(), order.getTotalAmount(), order.getFinalAmount(), order.getOrderedAt());
        }

        @Builder
        private Create(Long orderId, BigDecimal totalPrice, BigDecimal finalPrice, LocalDateTime orderedAt) {
            this.orderId = orderId;
            this.totalPrice = totalPrice;
            this.finalPrice = finalPrice;
            this.orderedAt = orderedAt;
        }

    }

    @Getter
    public static class ApplyCoupon {

        Long orderId;
        BigDecimal totalPrice;
        BigDecimal finalPrice;
        LocalDateTime orderedAt;

        public static ApplyCoupon from(Order order) {
            return new ApplyCoupon(order.getId(), order.getTotalAmount(), order.getFinalAmount(), order.getOrderedAt());
        }

        @Builder
        private ApplyCoupon(Long orderId, BigDecimal totalPrice, BigDecimal finalPrice, LocalDateTime orderedAt) {
            this.orderId = orderId;
            this.totalPrice = totalPrice;
            this.finalPrice = finalPrice;
            this.orderedAt = orderedAt;
        }
    }

    @Getter
    public static class OrderItems {
        List<OrderInfo.OrderItem> orderItems;

        public static OrderItems fromList(List<kr.hhplus.be.server.domain.order.OrderItem> orderItems) {
            return OrderItems.builder().orderItems(orderItems.stream().map(OrderItem::from).toList()).build();
        }

        @Builder
        private OrderItems(List<OrderInfo.OrderItem> orderItems) {
            this.orderItems = orderItems;
        }
    }


    @Getter
    public static class OrderItem {
        Long productId;
        BigDecimal price;
        Integer quantity;

        public static OrderItem from(kr.hhplus.be.server.domain.order.OrderItem orderItem){
            return OrderItem.builder().productId(orderItem.getProductId()).price(orderItem.getPrice()).quantity(orderItem.getQuantity()).build();
        }

        public static OrderItem of(Long productId, BigDecimal price, Integer quantity){
            return OrderItem.builder().productId(productId).price(price).quantity(quantity).build();
        }

        @Builder
        private OrderItem(Long productId, BigDecimal price, Integer quantity) {
            this.productId = productId;
            this.price = price;
            this.quantity = quantity;
        }
    }
}
