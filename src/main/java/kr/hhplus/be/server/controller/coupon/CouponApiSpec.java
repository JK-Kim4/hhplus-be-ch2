package kr.hhplus.be.server.controller.coupon;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "쿠폰", description = "쿠폰 생성, 사용자 쿠폰 발급")
public interface CouponApiSpec {

    @Operation(summary = "쿠폰 생성", description = "새로운 쿠폰을 생성합니다.")
    ResponseEntity<CouponCreateResponse> createCoupon(CouponCreateRequest request);


    @Operation(summary = "사용자 쿠폰 발급", description = "사용자 쿠폰을 발급합니다.")
    ResponseEntity<CouponIssueResponse> issueCoupon(Long couponId, Long userId);
}
