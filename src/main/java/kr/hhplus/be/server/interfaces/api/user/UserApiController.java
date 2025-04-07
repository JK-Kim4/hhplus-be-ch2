package kr.hhplus.be.server.interfaces.api.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserApiController implements UserApiSpec {

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse.Detail> findById(
            @PathVariable("id") Long userId) {
        return ResponseEntity.ok(new UserResponse.Detail());
    }

    @Override
    @GetMapping("/{id}/point")
    public ResponseEntity<UserResponse.UserPoint> findUserPointById(
            @PathVariable("id") Long userId) {
        return ResponseEntity.ok(new UserResponse.UserPoint());
    }

    @Override
    @GetMapping("/{id}/coupons")
    public ResponseEntity<UserResponse.UserCoupon> findUserCouponListByUserId(
            @PathVariable("id") Long userId) {
        return ResponseEntity.ok(new UserResponse.UserCoupon());
    }

    @Override
    @PatchMapping("/{id}/point")
    public ResponseEntity<UserResponse.UserPoint> chargePoint(
            @PathVariable("id") Long userId, Integer chargePoint) {
        return ResponseEntity.ok(new UserResponse.UserPoint());
    }
}
