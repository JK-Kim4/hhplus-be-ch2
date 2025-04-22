package kr.hhplus.be.server.domain.user;

import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public User charge(UserCommand.Charge command){
        User user = userRepository.findById(command.getUserId())
                .orElseThrow(NoResultException::new);
        user.chargePoint(command.getAmount());
        return user;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
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
}
