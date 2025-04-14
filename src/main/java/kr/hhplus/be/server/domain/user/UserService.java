package kr.hhplus.be.server.domain.user;

import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserInfo.Create create(UserCommand.Create command){
        Optional<User> user = userRepository.findByName(command.getName());

        if(user.isPresent()){
            throw new IllegalArgumentException("이미 존재하는 회원입니다.");
        }else{
            return UserInfo.Create.from(userRepository.save(new User(command.getName())));
        }
    }

    public UserInfo.User getUser(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(NoResultException::new);

        return UserInfo.User.from(user);
    }
}
