package kr.hhplus.be.server.interfaces.api.order;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.hhplus.be.server.application.order.OrderPaymentResult;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.order.command.OrderCreateCommand;
import kr.hhplus.be.server.domain.payment.Payment;

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

        @Schema(name = "totalPrice", description = "총 구매 가격", example = "10000")
        private Integer totalPrice;

        @Schema(name = "finalPaymentPrice", description = "최종 결제 가격", example = "9000")
        private Integer finalPaymentPrice;

        public Create(OrderCreateCommand.Response response) {
            Order order = response.getOrder();

            this.orderId = order.getId();
            this.userId = order.getOrderUser().getId();
            this.totalPrice = order.getTotalPrice();
            this.orderStatus = order.getOrderStatus();
            this.finalPaymentPrice = order.getFinalPaymentPrice();
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

        public Long getUserCouponId() {
            return userCouponId;
        }

        public OrderStatus getOrderStatus() {
            return orderStatus;
        }

        public Integer getTotalPrice() {
            return totalPrice;
        }

        public Integer getFinalPaymentPrice() {
            return finalPaymentPrice;
        }
    }

    public static class OrderPayment{

        private Order order;

        private Payment payment;

        public OrderPayment(OrderPaymentResult result){
            this.order = result.getOrder();
            this.payment = result.getPayment();
        }

    }
}
