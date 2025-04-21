package kr.hhplus.be.server.domain.user;

import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User charge(UserCommand.Charge command){
        User user = userRepository.findById(command.getUserId())
                .orElseThrow(NoResultException::new);
        user.chargePoint(command.getAmount());
        return userRepository.save(user);
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(NoResultException::new);
    }
}
