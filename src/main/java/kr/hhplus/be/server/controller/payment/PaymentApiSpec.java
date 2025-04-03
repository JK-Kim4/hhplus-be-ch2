package kr.hhplus.be.server.controller.payment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "결제", description = "결제 요청 처리")
public interface PaymentApiSpec {

    @Operation(summary = "결제 요청 처리", description = "결제 요청(paymentRequest)를 처리합니다.")
    ResponseEntity<PaymentProcessResponse> processPayment(PaymentProcessRequest paymentProcessRequest);
}
