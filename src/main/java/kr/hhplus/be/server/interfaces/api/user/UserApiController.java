package kr.hhplus.be.server.interfaces.api.user;

import kr.hhplus.be.server.application.user.UserCommandService;
import kr.hhplus.be.server.application.user.UserQueryService;
import kr.hhplus.be.server.domain.user.UserInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserApiController implements UserApiSpec {

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    public UserApiController(
            UserCommandService userCommandService,
            UserQueryService userQueryService) {
        this.userCommandService = userCommandService;
        this.userQueryService = userQueryService;
    }

    @Override
    @GetMapping("/{id}/point")
    public ResponseEntity<UserResponse.Point> findUserPointById(
            @PathVariable("id") Long userId) {

        UserInfo.Point result = userQueryService.findPointById(userId);
        return ResponseEntity.ok(UserResponse.Point.from(result));
    }

    @Override
    @PatchMapping("/{id}/point")
    public ResponseEntity<Void> chargePoint(
            @PathVariable("id") Long userId,
            @RequestBody UserRequest.Charge point) {

        userCommandService.charge(point.toCommand(userId));
        return ResponseEntity.ok().build();
    }
}
