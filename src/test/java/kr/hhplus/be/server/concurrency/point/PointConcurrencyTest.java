package kr.hhplus.be.server.concurrency.point;

import kr.hhplus.be.server.concurrency.support.ConcurrentTestExecutor;
import kr.hhplus.be.server.domain.user.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
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
        UserCommand.Deduct deductCommand = UserCommand.Deduct.of(testUser.getId(), 500);

        List<Runnable> tasks = List.of(
                () -> userService.charge(chargeCommand),
                () -> userService.deduct(deductCommand)
        );

        // when
        List<Throwable> errors = ConcurrentTestExecutor.execute(4,tasks);

        // then
        assertEquals(1, errors.size()); // 포인트 사용과 충전이 동시에 발생할 경우 낙관적 락으로 인해 후발 트랜잭션에서 Throw Exception
        assertEquals(ObjectOptimisticLockingFailureException.class, errors.get(0).getClass());  // 낙관적 락 반환 오류 검증
    }
}
