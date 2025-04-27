package kr.hhplus.be.server.interfaces.api.coupon;

import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.coupon.userCoupon.UserCouponInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coupons")
public class CouponApiController implements CouponApiSpec{

    private final CouponService couponService;

    public CouponApiController(CouponService couponService) {
        this.couponService = couponService;
    }

    @Override
    @PostMapping("/{id}/issue")
    public ResponseEntity<CouponResponse.Issue> issueCoupon(
            @PathVariable("id") Long couponId,
            @RequestBody CouponRequest.Issue request) {

        UserCouponInfo.Issue issue = couponService.issue(couponId, request.getUserId());
        return ResponseEntity.ok(CouponResponse.Issue.from(issue));
    }

    @Override
    @GetMapping("/user/{userId}")
    public ResponseEntity<CouponResponse.UserCouponList> findByUserId(
            @PathVariable("userId") Long userId) {

        UserCouponInfo.UserCouponList result = UserCouponInfo.UserCouponList.of(couponService.findByUserId(userId));
        return ResponseEntity.ok(CouponResponse.UserCouponList.from(result));
    }
}
