package kr.hhplus.be.server.interfaces.api.user;

import kr.hhplus.be.server.domain.user.UserInfo;
import kr.hhplus.be.server.domain.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserApiController implements UserApiSpec {

    private final UserService userService;

    public UserApiController(
            UserService userService) {
        this.userService = userService;
    }

    @Override
    @GetMapping("/{id}/point")
    public ResponseEntity<UserResponse.Point> findUserPointById(
            @PathVariable("id") Long userId) {

        UserInfo.Point result = userService.findPointById(userId);
        return ResponseEntity.ok(UserResponse.Point.from(result));
    }

    @Override
    @PatchMapping("/{id}/point")
    public ResponseEntity<Void> chargePoint(
            @PathVariable("id") Long userId,
            @RequestBody UserRequest.Charge point) {

        userService.charge(point.toCommand(userId));
        return ResponseEntity.ok().build();
    }
}
