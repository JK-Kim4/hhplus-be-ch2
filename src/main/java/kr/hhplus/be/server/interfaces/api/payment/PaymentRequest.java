package kr.hhplus.be.server.interfaces.api.payment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import kr.hhplus.be.server.application.orderPayment.criteria.OrderPaymentCriteria;
import lombok.Getter;

public class PaymentRequest {

    @Getter
    static public class Create{

        @NotNull @Positive
        private Long orderId;
        private Long userId;

        public OrderPaymentCriteria.PaymentCreate toCriteria() {
            return new OrderPaymentCriteria.PaymentCreate(this.orderId, this.userId);
        }

    }

    @Getter
    static public class Process {

        private Long orderId;
        private Long userId;
        private Long paymentId;

        public OrderPaymentCriteria.PaymentProcess toCriteria() {
            return new OrderPaymentCriteria.PaymentProcess(this.orderId, this.userId, this.paymentId);
        }
    }
}


