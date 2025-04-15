package kr.hhplus.be.server.interfaces.api.user;

import kr.hhplus.be.server.application.user.UserFacade;
import kr.hhplus.be.server.application.user.UserResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserApiController implements UserApiSpec {

    private final UserFacade userFacade;

    public UserApiController(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @Override
    @GetMapping("/{id}/point")
    public ResponseEntity<UserResponse.Point> findUserPointById(
            @PathVariable("id") Long userId) {

        UserResult.Point result
                = userFacade.findPointOrCreateDefaultByUserId(userId);
        return ResponseEntity.ok(UserResponse.Point.from(result));
    }

    @Override
    @PatchMapping("/{id}/point")
    public ResponseEntity<Void> chargePoint(
            @PathVariable("id") Long userId,
            @RequestBody UserRequest.Charge point) {

        userFacade.charge(point.toCriteria(userId));
        return ResponseEntity.ok().build();
    }
}
