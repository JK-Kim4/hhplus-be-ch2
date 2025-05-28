package kr.hhplus.be.server.interfaces.balance.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Balance", description = "사용자 잔액 관리")
public interface BalanceApiSpec {

    @Operation(summary = "사용자 잔액 조회",
            description = "사용자 고유번호를 전달받아 현재 잔액을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "잔액 조회 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BalanceResponse.class)))
    })
    ResponseEntity<BalanceResponse> findByUserId(BalanceRequest request);

    @Operation(summary = "사용자 잔액 충전",
            description = "사용자 고유번호와 충전 정보를 전달받아 잔액을 충전합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "잔액 충전 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BalanceResponse.Charge.class)))
    })
    ResponseEntity<BalanceResponse.Charge> charge(BalanceRequest.Charge request);

}
