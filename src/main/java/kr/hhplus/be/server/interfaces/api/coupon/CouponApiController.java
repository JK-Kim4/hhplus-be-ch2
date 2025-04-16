package kr.hhplus.be.server.interfaces.api.coupon;

import kr.hhplus.be.server.application.coupon.CouponFacade;
import kr.hhplus.be.server.domain.user.userCoupon.UserCouponInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<CouponResponse.Issue> issueCoupon(
            @PathVariable("id") Long couponId,
            @RequestBody CouponRequest.Issue request) {

        UserCouponInfo.Issue issue = couponFacade.issue(request.toCriteria(couponId));
        return ResponseEntity.ok(CouponResponse.Issue.from(issue));
    }

    @Override
    @GetMapping("/user/{userId}")
    public ResponseEntity<CouponResponse.UserCouponList> findByUserId(
            @PathVariable("userId") Long userId) {

        UserCouponInfo.UserCouponList result = couponFacade.findByUserId(userId);
        return ResponseEntity.ok(CouponResponse.UserCouponList.from(result));
    }
}
