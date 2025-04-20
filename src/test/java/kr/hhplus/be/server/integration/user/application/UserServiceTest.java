package kr.hhplus.be.server.integration.user.application;

import jakarta.persistence.NoResultException;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.domain.user.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Testcontainers
@Transactional
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    String name = "testuser";
    Long userId;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        User save = userRepository.save(User.createWithName(name));
        userId = save.getId();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void 사용자를_조회한다(){
        //when
        User savedUser = userService.findById(userId);

        //then
        assertEquals(name, savedUser.getName());
    }

    @Test
    void 없는_사용자를_조회시_오류발생(){
        //when
        Long nonExist = 999L;

        //then
        assertThrows(NoResultException.class,
                () -> userService.findById(nonExist));
    }
}
