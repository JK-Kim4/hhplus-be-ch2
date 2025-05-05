package kr.hhplus.be.server.application.user;

import jakarta.persistence.NoResultException;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserCommand;
import kr.hhplus.be.server.domain.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserCommandService {

    private final UserRepository userRepository;
    public UserCommandService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User charge(UserCommand.Charge command){
        User user = userRepository.findById(command.getUserId())
                .orElseThrow(NoResultException::new);
        user.chargePoint(command.getAmount());
        return user;
    }

    @Transactional
    public User deduct(UserCommand.Deduct command){
        User user = userRepository.findById(command.getUserId())
                .orElseThrow(NoResultException::new);
        user.deductPoint(command.getAmount());
        return user;
    }

    public void pay(UserCommand.Pay command) {
        User user = userRepository.findById(command.getUserId())
                .orElseThrow(NoResultException::new);

        user.deductPoint(command.getFinalPaymentPrice());
    }
}
