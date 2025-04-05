package kr.hhplus.be.server.controller.coupon;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coupons")
public class CouponApiController implements CouponApiSpec{

    @Override
    @PostMapping
    public ResponseEntity<CouponCreateResponse> createCoupon(
            @RequestBody CouponCreateRequest request) {
        return ResponseEntity.ok(new CouponCreateResponse());
    }

    @Override
    @PostMapping("/{id}/issue")
    public ResponseEntity<CouponIssueResponse> issueCoupon(
            @PathVariable("id") Long couponId,
            @RequestBody Long userId) {
        return ResponseEntity.ok(new CouponIssueResponse());
    }
}
