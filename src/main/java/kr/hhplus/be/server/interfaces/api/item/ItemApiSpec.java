package kr.hhplus.be.server.interfaces.api.item;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Item", description = "상품 상세 정보 조회, 인기 판매 순위 Top3 상품 목록 조회")
public interface ItemApiSpec {

    @Operation(summary = "상품 상세 정보 조회",
    description = "상품 고유 번호(itemId)에 해당하는 상품의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 조회 성공",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ItemResponse.Item.class))})
    })
    ResponseEntity<ItemResponse.Item> findById(@PathVariable(name = "itemId") Long id);

    ResponseEntity<ItemResponse.Rank> findItemRanking();
}
