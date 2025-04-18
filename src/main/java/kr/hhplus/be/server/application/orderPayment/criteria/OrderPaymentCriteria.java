package kr.hhplus.be.server.application.orderPayment.criteria;

import kr.hhplus.be.server.domain.order.command.OrderCommand;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderPaymentCriteria {

    private Long userId;
    private Long userCouponId;
    private List<OrderItemCriteria> orderItems;

    public OrderPaymentCriteria(Long userId, Long userCouponId, List<OrderItemCriteria> orderItems) {
        this.userId = userId;
        this.userCouponId = userCouponId;
        this.orderItems = orderItems;
    }

    public List<OrderCommand.OrderItemCreate> toOrderItemCreateCommand() {
        return orderItems.stream().map(OrderItemCriteria::toCommand).collect(Collectors.toList());
    }

    @Getter
    public static class PaymentCreate {

        private Long orderId;
        private Long userId;

        public static PaymentCreate of(Long orderId, Long userId) {
            return new PaymentCreate(orderId, userId);
        }

        public PaymentCreate(Long orderId, Long userId) {
            this.orderId = orderId;
            this.userId = userId;
        }

    }

    @Getter
    public static class PaymentProcess {

        private Long orderId;
        private Long userId;
        private Long paymentId;

        public static PaymentProcess of(Long orderId, Long userId, Long paymentId) {
            return new PaymentProcess(orderId, userId, paymentId);
        }

        public PaymentProcess(Long orderId, Long userId, Long paymentId) {
            this.paymentId = paymentId;
            this.userId = userId;
            this.orderId = orderId;
        }

    }
}
