package kr.hhplus.be.server.interfaces.coupon.api;

import kr.hhplus.be.server.application.coupon.CouponFacade;
import kr.hhplus.be.server.application.coupon.CouponResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/coupons")
public class CouponApiController implements CouponApiSpec {

    private final CouponFacade couponFacade;

    public CouponApiController(CouponFacade couponFacade) {
        this.couponFacade = couponFacade;
    }

    @Override
    @GetMapping("/user-coupon/user/{userId}")
    public ResponseEntity<CouponResponse.UserCoupons> issue(CouponRequest.UserCoupon request) {
        CouponResult.UserCoupons userCoupons = couponFacade.findUserCouponsByUserId(request.toCriteria());
        return ResponseEntity.ok(CouponResponse.UserCoupons.from(userCoupons));
    }

    @Override
    @PostMapping("/issue")
    public ResponseEntity<CouponResponse.RequestRegister> issue(
            @RequestBody CouponRequest.Issue request) {
        CouponResult.RequestRegister requestRegister = couponFacade.requestRegister(request.toRegisterCriteria());
        return ResponseEntity.ok(CouponResponse.RequestRegister.from(requestRegister));
    }
}
