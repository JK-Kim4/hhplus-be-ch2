package kr.hhplus.be.server.application.user;

import jakarta.persistence.NoResultException;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserInfo;
import kr.hhplus.be.server.domain.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class UserQueryService {

    private final UserRepository userRepository;

    public UserQueryService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(NoResultException::new);
    }

    public UserInfo.Point findPointById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(NoResultException::new);

        if(Objects.isNull(user.getPoint())){
            user.createPoint();
        }

        return UserInfo.Point.from(user);
    }
}
