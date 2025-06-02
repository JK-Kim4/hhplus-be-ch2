package kr.hhplus.be.server.interfaces.coupon.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Coupon", description = "쿠폰 관리")
public interface CouponApiSpec {

    @Operation(summary = "쿠폰 등록",
            description = "신규 쿠폰을 등록한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "쿠폰 등록 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CouponResponse.Create.class)))
    })
    ResponseEntity<CouponResponse.Create> create(CouponRequest.Create request);


    @Operation(summary = "사용자 쿠폰 조회",
            description = "사용자 고유 정보를 전달받아 발급받은 쿠폰 목록을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "쿠폰 발급 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CouponResponse.UserCoupon.class)))
    })
    ResponseEntity<CouponResponse.UserCoupons> issue(CouponRequest.UserCoupon request);


    @Operation(summary = "사용자 쿠폰 발급(Redis)",
            description = "쿠폰 발급 정보를 전달받아 사용자 쿠폰을 발급정보를 Redis에 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "쿠폰 발급 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CouponResponse.Issue.class)))
    })
    ResponseEntity<CouponResponse.RequestRegister> issue(CouponRequest.Issue request);

    @Operation(summary = "사용자 쿠폰 발급 V2(Kafka)",
            description = "쿠폰 발급 정보를 전달받아 사용자 쿠폰을 발급정보를 Redis에 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "쿠폰 발급 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CouponResponse.Void.class)))
    })
    ResponseEntity<CouponResponse.Void> issueV2(CouponRequest.Issue request);


}
