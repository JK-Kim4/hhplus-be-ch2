package kr.hhplus.be.server.integration.point.infrastructure;

import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.point.PointRepository;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
public class PointRepositoryTest {

    @Autowired
    PointRepository pointRepository;

    @Autowired
    UserRepository userRepository;

    User user;
    Long pointId;

    @BeforeEach
    void setUp() {
        user = new User("tester");
        userRepository.save(user);

        Point point = Point.create(user);
        pointRepository.save(point);

        pointId = point.getId();
    }

    @Test
    void 포인트를_저장한다(){
        //given
        User user = new User("tester name");
        userRepository.save(user);

        Point point = Point.create(user);
        pointRepository.save(point);

        //when
        Point savedPoint = pointRepository.findById(point.getId()).get();

        //then
        assertEquals(point, savedPoint);
    }

    @Test
    void 포인트를_고유번호로_조회한다(){
        //when
        Point point = pointRepository.findById(pointId).get();

        //then
        assertEquals( user, point.user());
    }



}
