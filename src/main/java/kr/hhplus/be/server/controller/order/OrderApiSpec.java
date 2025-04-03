package kr.hhplus.be.server.controller.order;

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
            content = {@Content(schema = @Schema(implementation = OrderCreateResponse.class))}),
            @ApiResponse(responseCode = "400", description = "미결제 주문이 존재하는 경우"),
            @ApiResponse(responseCode = "400", description = "전달된 상품 가격이 현재 가격과 일치하지 않는 경우"),
            @ApiResponse(responseCode = "400", description = "상품 재고가 부족할 경우")
    })
    ResponseEntity<OrderCreateResponse> createOrder(OrderCreateRequest request);
}
