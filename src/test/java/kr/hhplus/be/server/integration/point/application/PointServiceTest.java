package kr.hhplus.be.server.integration.point.application;

import jakarta.persistence.NoResultException;
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.point.PointCommand;
import kr.hhplus.be.server.domain.point.PointService;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Testcontainers
public class PointServiceTest {

    @Autowired
    PointService pointService;

    @Autowired
    UserRepository userRepository;

    User user;

    @BeforeEach
    void setUp() {
        user = userRepository.save(new User("tester"));
    }

    @Test
    void 포인트를_충전한다(){
        //given
        PointCommand.Charge command = PointCommand.Charge.of(user.getId(), 5000);

        //when
        pointService.charge(command);
        User chargedUser = userRepository.findById(user.getId()).get();

        //then
        assertEquals(5000, chargedUser.point());
    }

    @Test
    void 사용자포인트가_존재하지않을경우_기본포인트를_생성한다(){
        //when
        Point userPoint = pointService.findPointByUserId(user.getId());

        //then
        assertEquals(user, userPoint.getUser());
        assertEquals(0, userPoint.getAmount());
    }

    @Test
    void 사용자가_존재하지않을경우_오류를반환한다(){
        //then
        assertThrows(NoResultException.class,
                () -> pointService.findPointByUserId(50L));
    }
}
