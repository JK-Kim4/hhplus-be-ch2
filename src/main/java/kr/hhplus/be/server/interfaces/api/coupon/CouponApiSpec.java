package kr.hhplus.be.server.interfaces.api.coupon;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Coupon", description = "쿠폰 관리")
public interface CouponApiSpec {

    @Operation(summary = "사용자 쿠폰 발급",
            description = "쿠폰 발급 정보를 전달받아 사용자 쿠폰을 발급합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "쿠폰 발급 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CouponResponse.Issue.class)))
    })
    ResponseEntity<CouponResponse.Issue> issue(CouponRequest.Issue request);

    @Operation(summary = "사용자 쿠폰 발급(Redis)",
            description = "쿠폰 발급 정보를 전달받아 사용자 쿠폰을 발급정보를 Redis에 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "쿠폰 발급 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CouponResponse.Issue.class)))
    })
    ResponseEntity<CouponResponse.RequestIssue> issueV2(CouponRequest.Issue request);
}
