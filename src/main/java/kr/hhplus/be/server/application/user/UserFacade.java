package kr.hhplus.be.server.application.user;

import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.point.PointCommand;
import kr.hhplus.be.server.domain.point.PointService;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserFacade {

    private final UserService userService;
    private final PointService pointService;

    public UserFacade(UserService userService, PointService pointService) {
        this.userService = userService;
        this.pointService = pointService;
    }

    public void charge(UserCriteria.Charge criteria) {
        User user = userService.findById(criteria.getUserId());
        pointService.charge(PointCommand.Charge.of(user.getId(), criteria.getChargeAmount()));
    }

    public UserResult.Point findPointOrCreateDefaultByUserId(Long userId){
        Point point = pointService.findPointByUserId(userId);
        return UserResult.Point.from(point);
    }
}
