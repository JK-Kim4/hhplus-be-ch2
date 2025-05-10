package kr.hhplus.be.server.domain.user;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserInfo.OptionalUser findByName(String name){

        return UserInfo.OptionalUser.of(userRepository.findByName(name));
    }

    public UserInfo.Create create(UserCommand.Create command){
        User user = userRepository.save(User.builder()
                .name(command.getName())
                .build());

        return UserInfo.Create.from(user);
    }
}
