package kr.hhplus.be.server.integration.user.infrastructure;

import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    private Long userId;
    private String username;

    @BeforeEach
    void setUp() {
        User user = new User("test");
        userRepository.save(user);
        userId = user.getId();
        username = user.getName();
    }

    @Test
    void save_user_test(){
        //give
        User user = new User("save user");

        //when
        userRepository.save(user);
        User savedUser = userRepository.findById(user.getId()).get();

        //then
        Assertions.assertEquals(user, savedUser);
    }

    @Test
    void find_user_test() {
        //when
        User savedUser = userRepository.findById(userId).get();

        //then
        Assertions.assertEquals(username, savedUser.getName());
    }

    @Test
    void find_by_id_test(){
        //when
        User savedUser = userRepository.findByName(username).get();

        //then
        Assertions.assertEquals(username, savedUser.getName());
    }
}
