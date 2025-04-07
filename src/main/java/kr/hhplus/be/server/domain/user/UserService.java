package kr.hhplus.be.server.domain.user;

import jakarta.persistence.NoResultException;
import kr.hhplus.be.server.infrastructure.user.FakeUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(FakeUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public User findById(UserCommand.Find find) {

        return userRepository.findById(find.getUserId())
                .orElseThrow(NoResultException::new);
    }

    @Transactional(readOnly = true)
    public Integer findPointById(UserCommand.Find find) {

        User user = userRepository.findById(find.getUserId())
                .orElseThrow(NoResultException::new);

        return user.point();
    }

    @Transactional
    public Integer charge(UserCommand.Charge chargeCommand) {

        User user = userRepository.findById(chargeCommand.getUserId())
                .orElseThrow(NoResultException::new);

        user.chargePoint(chargeCommand.getAmount());

        return userRepository.insertOrUpdate(user).point();
    }
}
