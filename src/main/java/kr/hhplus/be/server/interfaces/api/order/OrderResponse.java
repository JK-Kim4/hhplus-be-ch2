package kr.hhplus.be.server.interfaces.api.order;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.hhplus.be.server.domain.order.OrderStatus;

import java.util.List;

public class OrderResponse {

    static public class Create{

        public Create(){}

        @Schema(name = "orderId", description = "주문 고유 번호", example = "5")
        private Long orderId;

        @Schema(name = "userId", description = "사용자 고유 번호", example = "5")
        private Long userId;

        @Schema(name = "couponId", description = "(선택) 사용 쿠폰 고유 번호", example = "24")
        private Long userCouponId;

        @Schema(name = "orderStatus", description = "주문 상태", example = "ORDERED")
        private OrderStatus orderStatus;

        @Schema(name = "orderItems", description = "주문 상품 목록", example = "")
        private List<OrderItemResponse> orderItems;

        @Schema(name = "totalPrice", description = "총 구매 가격", example = "10000")
        private Integer totalPrice;

        @Schema(name = "finalPaymentPrice", description = "최종 결제 가격", example = "9000")
        private Integer finalPaymentPrice;

        public Create(
                Long orderId, Long userId,
                Long userCouponId, OrderStatus orderStatus,
                List<OrderItemResponse> orderItems, Integer totalPrice,
                Integer finalPaymentPrice) {
            this.orderId = orderId;
            this.userId = userId;
            this.userCouponId = userCouponId;
            this.orderStatus = orderStatus;
            this.orderItems = orderItems;
            this.totalPrice = totalPrice;
            this.finalPaymentPrice = finalPaymentPrice;
        }

        public Long getOrderId() {
            return orderId;
        }

        public void setOrderId(Long orderId) {
            this.orderId = orderId;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Long getUserCouponId() {
            return userCouponId;
        }

        public void setUserCouponId(Long userCouponId) {
            this.userCouponId = userCouponId;
        }

        public OrderStatus getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(OrderStatus orderStatus) {
            this.orderStatus = orderStatus;
        }

        public List<OrderItemResponse> getOrderItems() {
            return orderItems;
        }

        public void setOrderItems(List<OrderItemResponse> orderItems) {
            this.orderItems = orderItems;
        }

        public Integer getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(Integer totalPrice) {
            this.totalPrice = totalPrice;
        }

        public Integer getFinalPaymentPrice() {
            return finalPaymentPrice;
        }

        public void setFinalPaymentPrice(Integer finalPaymentPrice) {
            this.finalPaymentPrice = finalPaymentPrice;
        }
    }
}
