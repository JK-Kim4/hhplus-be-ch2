package kr.hhplus.be.server.domain.payment.command;

import kr.hhplus.be.server.domain.payment.Payment;

public class PaymentCreateCommand {

    private Long orderId;
    private Long userId;
    private Long userCouponId;

    public PaymentCreateCommand() {}

    public PaymentCreateCommand(Long orderId) {
        this.orderId = orderId;
    }

    public PaymentCreateCommand(Long orderId, Long userCouponId) {
        this.orderId = orderId;
        this.userCouponId = userCouponId;
    }

    public static PaymentCreateCommand of(Long orderId) {
        return new PaymentCreateCommand(orderId);
    }

    public Long getOrderId() {
        return orderId;
    }

    public static class Response {

        private Payment payment;

        private Long paymentId;

        public Response(Payment payment) {
            this.payment = payment;
        }

        public Payment getPayment() {
            return payment;
        }

        public Response(Long paymentId) {
            this.paymentId = paymentId;
        }

        public Long getPaymentId() {
            return paymentId;
        }
    }
}
