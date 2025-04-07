package kr.hhplus.be.server.interfaces.api.item;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ItemResponse.Detail.class))})
    })
    ResponseEntity<ItemResponse.Detail> findById(@PathVariable(name = "itemId") Long id);


    @Operation(summary = "최대 판매 상품 TOP3 조회",
    description = "현재 일시 기준 지난 3일간 가장 많이 팔린 상품 TOP3 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "최다 판매 상품 목록 조회 성공",
                    content = {@Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(name = "최대 판매 상품 TOP3 조회 응답",
                                    value = """
                    [
                        {
                            "itemId": 2,
                            "name": "자전거",
                            "price": 10000,
                            "orderCount": 4552
                        },
                        {
                            "itemId": 1,
                            "name": "자동차",
                            "price": 30000,
                            "orderCount": 2000
                        },
                        {
                            "itemId": 99,
                            "name": "비행기",
                            "price": 60000,
                            "orderCount": 520
                        }
                    ]
                """
                            ))}
            )
    })
    ResponseEntity<List<ItemResponse.Rank>> findItemRanking();
}
