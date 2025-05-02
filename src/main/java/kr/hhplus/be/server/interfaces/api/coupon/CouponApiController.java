package kr.hhplus.be.server.interfaces.api.coupon;

import kr.hhplus.be.server.application.coupon.CouponCommandService;
import kr.hhplus.be.server.application.coupon.CouponInfo;
import kr.hhplus.be.server.application.coupon.CouponQueryService;
import kr.hhplus.be.server.domain.coupon.userCoupon.UserCouponInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coupons")
public class CouponApiController implements CouponApiSpec{

    private final CouponQueryService couponQueryService;
    private final CouponCommandService couponCommandService;

    public CouponApiController(
            CouponCommandService couponCommandService,
            CouponQueryService couponQueryService) {
        this.couponCommandService = couponCommandService;
        this.couponQueryService = couponQueryService;
    }

    @Override
    @PostMapping("/{id}/issue")
    public ResponseEntity<CouponResponse.Issue> issueCoupon(
            @PathVariable("id") Long couponId,
            @RequestBody CouponRequest.Issue request) {

        UserCouponInfo.Issue issue = couponCommandService.issue(request.toCommand(couponId));
        return ResponseEntity.ok(CouponResponse.Issue.from(issue));
    }

    @Override
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CouponResponse.UserCoupon>> findByUserId(
            @PathVariable("userId") Long userId) {

        List<CouponInfo.UserCouponInfo> result = couponQueryService.findByUserId(userId);
        return ResponseEntity.ok(CouponResponse.UserCoupon.fromList(result));
    }
}
