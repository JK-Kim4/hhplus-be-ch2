package kr.hhplus.be.server.domain.user;

import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User charge(UserCommand.Charge command){
        User user = userRepository.findById(command.getUserId())
                .orElseThrow(NoResultException::new);
        user.chargePoint(command.getAmount());
        return userRepository.save(user);
    }

    @Transactional
    public User deduct(UserCommand.Deduct command){
        User user = userRepository.findById(command.getUserId())
                .orElseThrow(NoResultException::new);
        user.deductPoint(command.getAmount());
        return user;
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
