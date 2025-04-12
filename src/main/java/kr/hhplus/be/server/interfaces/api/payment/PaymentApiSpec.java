package kr.hhplus.be.server.interfaces.api.payment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Payment", description = "결제 요청 처리")
public interface PaymentApiSpec {

    @Operation(summary = "결제 요청 처리", description = "결제 요청(paymentRequest)를 처리합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "결제 처리 요청 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentProcessResponse.class))),
    })
    ResponseEntity<PaymentProcessResponse> processPayment(PaymentProcessRequest paymentProcessRequest);
}
