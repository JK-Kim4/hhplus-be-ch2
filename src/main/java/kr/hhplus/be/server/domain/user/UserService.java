package kr.hhplus.be.server.domain.user;

import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User save(UserCommand.Create command){
        Optional<User> user = userRepository.findByName(command.getName());

        if(user.isPresent()){
            throw new IllegalArgumentException("이미 존재하는 회원입니다.");
        }else{
            return userRepository.save(new User(command.getName()));
        }
    }

    @Transactional
    public User charge(UserCommand.Charge command){
        User user = userRepository.findById(command.getUserId())
                .orElseThrow(NoResultException::new);
        user.chargePoint(command.getAmount());
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(NoResultException::new);
    }


}
