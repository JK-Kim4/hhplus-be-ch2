package kr.hhplus.be.server.interfaces.user.api;

import kr.hhplus.be.server.application.user.UserFacade;
import kr.hhplus.be.server.application.user.UserResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserApiController implements UserApiSpec{

    private final UserFacade userFacade;

    public UserApiController(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @Override
    @PostMapping
    public ResponseEntity<UserResponse.Create> create(
            @RequestBody UserRequest.Create request) {
        UserResult.Create user = userFacade.create(request.toCriteria());

        return ResponseEntity.ok(UserResponse.Create.from(user));
    }

    @Override
    @GetMapping("/{userId}/coupons")
    public ResponseEntity<UserResponse.Coupons> coupons(
            @PathVariable Long userId) {
        UserResult.Coupons coupons = userFacade.findCouponsByUserId(userId);
        return ResponseEntity.ok(UserResponse.Coupons.from(coupons.getCoupons()));
    }
}
