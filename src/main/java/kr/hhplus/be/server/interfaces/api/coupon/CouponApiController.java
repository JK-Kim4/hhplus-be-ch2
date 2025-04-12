package kr.hhplus.be.server.interfaces.api.coupon;

import kr.hhplus.be.server.application.coupon.CouponFacade;
import kr.hhplus.be.server.domain.userCoupon.UserCoupon;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coupons")
public class CouponApiController implements CouponApiSpec{


    private final CouponFacade couponFacade;

    public CouponApiController(CouponFacade couponFacade) {
        this.couponFacade = couponFacade;
    }

    @Override
    @PostMapping
    public ResponseEntity<CouponResponse.Create> createCoupon(
            @RequestBody CouponRequest.Create request) {
        return ResponseEntity.ok(new CouponResponse.Create());
    }

    @Override
    @PostMapping("/{id}/issue")
    public ResponseEntity<CouponIssueResponse> issueCoupon(
            @PathVariable("id") Long couponId,
            @RequestBody CouponIssueRequest request) {
        return ResponseEntity.ok(
                CouponIssueResponse.from(couponFacade.issue(request.toUserCouponIssueCommand(couponId))));
    }

    @Override
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserCoupon>> findByUserId(
            @PathVariable("userId") Long userId) {
        return ResponseEntity.ok(couponFacade.findByUserId(userId));
    }
}
