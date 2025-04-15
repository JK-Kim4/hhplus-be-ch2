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

        public static PaymentCreate of(Long orderId) {
            return new PaymentCreate(orderId);
        }

        public PaymentCreate(Long orderId) {
            this.orderId = orderId;
        }
    }

    @Getter
    public static class PaymentProcess {

        private Long paymentId;

        public static PaymentProcess of(Long paymentId) {
            return new PaymentProcess(paymentId);
        }

        public PaymentProcess(Long paymentId) {
            this.paymentId = paymentId;
        }

    }
}
