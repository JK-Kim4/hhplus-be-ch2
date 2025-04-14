package kr.hhplus.be.server.application.user;

import kr.hhplus.be.server.domain.point.PointCommand;
import kr.hhplus.be.server.domain.point.PointService;
import kr.hhplus.be.server.domain.user.UserInfo;
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
        UserInfo.User user = userService.getUser(criteria.getUserId());
        pointService.charge(PointCommand.Charge.of(user.getUserId(), criteria.getChargeAmount()));
    }

    public UserResult.Point getPoint(Long userId){
        return UserResult.Point.from(pointService.getPoint(userId));
    }
}
