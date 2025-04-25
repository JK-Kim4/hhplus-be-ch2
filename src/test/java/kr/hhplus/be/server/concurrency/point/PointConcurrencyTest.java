package kr.hhplus.be.server.concurrency.point;

import kr.hhplus.be.server.concurrency.support.ConcurrentTestExecutor;
import kr.hhplus.be.server.domain.user.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
public class

PointConcurrencyTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        User user = UserTestFixture.createTestUser();
        user.chargePoint(50_000);
        testUser = userRepository.save(user);
    }

    @RepeatedTest(10)
    void 사용자포인트_사용_충전_동시성테스트() throws InterruptedException{

        // given
        UserCommand.Charge chargeCommand = new UserCommand.Charge(testUser.getId(), 500);
        UserCommand.Deduct deductCommand = new UserCommand.Deduct(testUser.getId(), 500);

        List<Runnable> tasks = List.of(
                () -> userService.charge(chargeCommand),
                () -> userService.deduct(deductCommand)
        );

        // when 각 TASK를 100번씩 실행하기 위해 200개의 스레드 생성
        ConcurrentTestExecutor.execute(4,tasks);

        // then
        User updatedUser = userRepository.findById(testUser.getId()).orElseThrow();

        System.out.println("최종 포인트: " + updatedUser.point());

        assertEquals(50_000, updatedUser.point());
    }
}
