package kr.hhplus.be.server.interfaces.api.order;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Order", description = "주문 생성")
public interface OrderApiSpec {

    @Operation(summary = "주문 생성",
            description = "주문 생성 정보(orderCreateRequest)를 전달받아 신규 주문을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주문 생성 성공",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = OrderRequest.Create.class))})
    })
    ResponseEntity<OrderResponse.Create> createOrder(OrderRequest.Create request);
}
