package kr.hhplus.be.server.controller.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserApiController implements UserApiSpec {

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(
            @PathVariable("id") Long userId) {
        return ResponseEntity.ok(new UserResponse());
    }

    @Override
    @GetMapping("/{id}/point")
    public ResponseEntity<UserPointResponse> findUserPointById(
            @PathVariable("id") Long userId) {
        return ResponseEntity.ok(new UserPointResponse());
    }

    @Override
    @GetMapping("/{id}/coupons")
    public ResponseEntity<UserCouponResponse> findUserCouponListByUserId(
            @PathVariable("id") Long userId) {
        return ResponseEntity.ok(new UserCouponResponse());
    }

    @Override
    @PatchMapping("/{id}/point")
    public ResponseEntity<UserPointResponse> chargePoint(
            @PathVariable("id") Long userId, Integer chargePoint) {
        return ResponseEntity.ok(new UserPointResponse());
    }
}
