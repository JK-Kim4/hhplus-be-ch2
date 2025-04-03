package kr.hhplus.be.server.controller.item;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = "Item", description = "상품 상세 정보 조회, 인기 판매 순위 Top3 상품 목록 조회")
public interface ItemApiSpec {

    @Operation(summary = "상품 상세 정보 조회",
    description = "상품 고유 번호(itemId)에 해당하는 상품의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 조회 성공",
            content = {@Content(schema = @Schema(implementation = ItemResponse.class))}),
            @ApiResponse(responseCode = "400", description = "올바르지 않은 요청"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 상품")

    })
    ResponseEntity<ItemResponse> findById(@PathVariable(name = "itemId") Long id);


    @Operation(summary = "최대 한매 상품 TOP3 조회",
    description = "현재 일시 기준 지난 3일간 가장 많이 팔린 상품 TOP3 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "최다 판매 상품 목록 조회 성공",
            content = {@Content(schema = @Schema(implementation = ItemRankResponse.class))}),
            @ApiResponse(responseCode = "500", description = "통계 데이터가 존재하지 않음")
    })
    ResponseEntity<List<ItemRankResponse>> findItemRanking();
}
