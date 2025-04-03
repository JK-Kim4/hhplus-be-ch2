package kr.hhplus.be.server.controller.coupon;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Coupon", description = "쿠폰 생성, 사용자 쿠폰 발급")
public interface CouponApiSpec {

    @Operation(summary = "쿠폰 생성", description = "새로운 쿠폰을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "쿠폰 생성 성공",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CouponCreateResponse.class))})
    })
    ResponseEntity<CouponCreateResponse> createCoupon(CouponCreateRequest request);


    @Operation(summary = "사용자 쿠폰 발급", description = "사용자 쿠폰을 발급합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "쿠폰 생성 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CouponIssueResponse.class))})
    })
    ResponseEntity<CouponIssueResponse> issueCoupon(
        Long couponId,
        @RequestBody(content =
        @Content(mediaType = "application/json", examples = {@ExampleObject(name = "사용자 고유번호", value = "10")})) Long userId);
}
