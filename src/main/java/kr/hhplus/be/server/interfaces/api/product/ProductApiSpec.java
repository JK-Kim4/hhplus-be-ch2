package kr.hhplus.be.server.interfaces.api.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Product", description = "상품 관리")
public interface ProductApiSpec {

    @Operation(summary = "상품 목록 조회",
            description = "등록되어있는 상품의 목록을 조회합니다. (OFFSET, LIMIT 선택 옵션)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 목록 조회 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponse.Products.class)))
    })
    ResponseEntity<ProductResponse.Products> products(int offset, int limit);


    @Operation(summary = "익일 기준 인기 판매 상품 TOP3 조회",
            description = "익일 기준 인기 판매 상품 상위 3개 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "목록 조회 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponse.Ranks.class)))
    })
    ResponseEntity<ProductResponse.Ranks> last3DaysRank();


    @Operation(summary = "실시간 인기 판매 상품 TOP3 조회",
            description = "실시간 인기 판매 상품 TOP3 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "목록 조회 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponse.Ranks.class)))
    })
    ResponseEntity<ProductResponse.Ranks> realTimeRank();
}
