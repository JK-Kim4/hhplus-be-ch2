package kr.hhplus.be.server.interfaces.api.coupon;

import kr.hhplus.be.server.application.coupon.CouponFacade;
import kr.hhplus.be.server.application.coupon.CouponResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/coupons")
public class CouponApiController implements CouponApiSpec {

    private final CouponFacade couponFacade;

    public CouponApiController(CouponFacade couponFacade) {
        this.couponFacade = couponFacade;
    }

    @Override
    @PostMapping("/issue")
    public ResponseEntity<CouponResponse.Issue> issue(
            @RequestBody CouponRequest.Issue request) {

        CouponResult.Issue issue = couponFacade.issue(request.toCriteria());
        return ResponseEntity.ok(CouponResponse.Issue.from(issue));
    }

    @Override
    @PostMapping("/issueV2")
    public ResponseEntity<CouponResponse.RequestIssue> issueV2(
            @RequestBody CouponRequest.Issue request) {
        CouponResult.RequestIssue requestIssue = couponFacade.requestIssue(request.toCriteria());
        return ResponseEntity.ok(CouponResponse.RequestIssue.from(requestIssue));
    }
}
