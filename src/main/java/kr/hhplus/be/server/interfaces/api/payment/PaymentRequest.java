package kr.hhplus.be.server.interfaces.api.payment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import kr.hhplus.be.server.domain.payment.command.PaymentCreateCommand;
import kr.hhplus.be.server.domain.payment.command.PaymentProcessCommand;

public class PaymentRequest {

    static public class Create{

        @NotNull @Positive
        private Long orderId;

        public Long getOrderId() {
            return orderId;
        }

        public PaymentCreateCommand toCommand(){
            return new PaymentCreateCommand(orderId);
        }
    }

    static public class Process {

        @NotNull @Positive
        private Long paymentId;

        public Long getPaymentId() {
            return paymentId;
        }

        public PaymentProcessCommand toCommand(){
            return new PaymentProcessCommand(paymentId);
        }
    }
}


