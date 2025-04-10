package kr.hhplus.be.server.interfaces.api.coupon;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.domain.userCoupon.UserCoupon;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Coupon", description = "쿠폰 생성, 사용자 쿠폰 발급")
public interface CouponApiSpec {

    @Operation(summary = "쿠폰 생성", description = "새로운 쿠폰을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "쿠폰 생성 성공",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CouponResponse.Create.class))})
    })
    ResponseEntity<CouponResponse.Create> createCoupon(CouponRequest.Create request);


    @Operation(summary = "사용자 쿠폰 발급", description = "사용자 쿠폰을 발급합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "쿠폰 생성 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CouponIssueRequest.class))})
    })
    ResponseEntity<CouponIssueResponse> issueCoupon(
        Long couponId, CouponIssueRequest request);

    @Operation(summary = "사용자 쿠폰 목록 ㅈ도회", description = "사용자 쿠폰 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "쿠폰 조회 성공")
    })
    ResponseEntity<List<UserCoupon>> findByUserId(Long userId);

}
