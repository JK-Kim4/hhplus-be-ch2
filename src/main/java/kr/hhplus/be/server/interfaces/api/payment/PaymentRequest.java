package kr.hhplus.be.server.interfaces.api.payment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import kr.hhplus.be.server.application.orderPayment.criteria.OrderPaymentCriteria;

public class PaymentRequest {

    static public class Create{

        @NotNull @Positive
        private Long orderId;

        public Long getOrderId() {
            return orderId;
        }

        public OrderPaymentCriteria.PaymentCreate toCriteria() {
            return new OrderPaymentCriteria.PaymentCreate(this.orderId);
        }

    }

    static public class Process {

        @NotNull @Positive
        private Long paymentId;

        public Long getPaymentId() {
            return paymentId;
        }

        public OrderPaymentCriteria.PaymentProcess toCriteria() {
            return new OrderPaymentCriteria.PaymentProcess(this.paymentId);
        }
    }
}


