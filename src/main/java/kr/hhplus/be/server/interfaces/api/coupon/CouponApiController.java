package kr.hhplus.be.server.interfaces.api.coupon;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coupons")
public class CouponApiController implements CouponApiSpec{

    @Override
    @PostMapping
    public ResponseEntity<CouponResponse.Create> createCoupon(
            @RequestBody CouponRequest.Create request) {
        return ResponseEntity.ok(new CouponResponse.Create());
    }

    @Override
    @PostMapping("/{id}/issue")
    public ResponseEntity<CouponResponse.Issue> issueCoupon(
            @PathVariable("id") Long couponId,
            @RequestBody Long userId) {
        return ResponseEntity.ok(new CouponResponse.Issue());
    }
}
