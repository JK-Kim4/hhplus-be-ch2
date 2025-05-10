package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.domain.payment.PaymentCommand;
import lombok.Builder;
import lombok.Getter;

public class PaymentCriteria {

    @Getter
    public static class Pay {
        Long orderId;
        Long userId;

        public static Pay of(Long orderId, Long userId){
            return Pay.builder().orderId(orderId).userId(userId).build();
        }

        public PaymentCommand.Create toPaymentCreateCommand(){
            return PaymentCommand.Create.of(this.orderId);
        }

        public PaymentCommand.Pay toPaymentPayCommand(Long paymentId){
            return PaymentCommand.Pay.of(this.userId, paymentId);
        }

        @Builder
        private Pay(Long orderId, Long userId){
            this.orderId = orderId;
            this.userId = userId;
        }

    }
}
