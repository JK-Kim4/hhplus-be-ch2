package kr.hhplus.be.server.domain.payment;

public class PaymentCreateCommand {

    private Long orderId;

    public PaymentCreateCommand() {}

    public PaymentCreateCommand(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public static class Response {

        private Payment payment;

        public Response(Payment payment) {
            this.payment = payment;
        }

        public Payment getPayment() {
            return payment;
        }
    }
}
