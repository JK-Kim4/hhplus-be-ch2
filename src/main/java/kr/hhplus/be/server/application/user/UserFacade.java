package kr.hhplus.be.server.application.user;

import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Component
public class UserFacade {

    private final UserService userService;

    public UserFacade(UserService userService) {
        this.userService = userService;
    }


    @Transactional
    public void charge(UserCriteria.Charge criteria) {
        userService.charge(criteria.toCommand());
    }

    @Transactional(readOnly = true)
    public UserResult.Point findPointById(Long userId){
        User user = userService.findById(userId);

        if(Objects.isNull(user.getPoint())){
            user.createPoint();
        }

        return UserResult.Point.from(user.getPoint());
    }
}
