package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.item.Item;
import kr.hhplus.be.server.domain.item.ItemInfo;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class OrderCommand {

    @Getter
    public static class RegisterPayment{

        Long orderId;
        Long paymentId;

        public static RegisterPayment of(Long orderId, Long paymentId) {
            return new RegisterPayment(orderId, paymentId);
        }

        private RegisterPayment(Long orderId, Long paymentId) {
            this.orderId = orderId;
            this.paymentId = paymentId;
        }

    }

    @Getter
    public static class CreateV2 {
        Long userId;
        Long userCouponId;
        List<ItemInfo.OrderItem> orderItems;

        public static CreateV2 of(Long userId, Long userCouponId, List<ItemInfo.OrderItem> orderItems) {
            return new CreateV2(userId, userCouponId, orderItems);
        }

        private CreateV2(Long userId, Long userCouponId, List<ItemInfo.OrderItem> orderItems) {
            this.userId = userId;
            this.userCouponId = userCouponId;
            this.orderItems = orderItems;
        }

    }

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
