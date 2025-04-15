package kr.hhplus.be.server.domain.payment;

public class PaymentCommand {

    public static class Create{
        private Payment payment;

        public static Create of(Payment payment) {
            return new Create(payment);
        }

        public Payment getPayment() {
            return payment;
        }

        public Create(Payment payment) {
            this.payment = payment;
        }
    }
}
