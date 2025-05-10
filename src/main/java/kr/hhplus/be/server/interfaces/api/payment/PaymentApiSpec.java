package kr.hhplus.be.server.interfaces.api.payment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Payment", description = "결제 관리")
public interface PaymentApiSpec {

    @Operation(summary = "결제 진행",
            description = "주문 정보를 전달받아 결제를 진행합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "결제 처리 완료",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResponse.Pay.class)))
    })
    ResponseEntity<PaymentResponse.Pay> pay(PaymentRequest.Pay request);
}
