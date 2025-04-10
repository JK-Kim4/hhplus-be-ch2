package kr.hhplus.be.server.interfaces.api.user;

import kr.hhplus.be.server.domain.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserApiController implements UserApiSpec {

    private final UserService userService;

    public UserApiController(UserService userService) {
        this.userService = userService;
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse.Detail> findById(
            @PathVariable("id") Long userId) {
        return ResponseEntity.ok(new UserResponse.Detail(userService.findById(userId)));
    }

    @Override
    @GetMapping("/{id}/point")
    public ResponseEntity<UserResponse.Point> findUserPointById(
            @PathVariable("id") Long userId) {
        return ResponseEntity.ok(new UserResponse.Point(userService.findById(userId)));
    }

    @Override
    @PatchMapping("/{id}/point")
    public ResponseEntity<UserResponse.Point> chargePoint(
            @PathVariable("id") Long userId,
            @RequestBody UserRequest.Charge point) {
        return ResponseEntity.ok(
                new UserResponse.Point(userId, userService.charge(point.toCommand(userId)).getPoint()));
    }
}
