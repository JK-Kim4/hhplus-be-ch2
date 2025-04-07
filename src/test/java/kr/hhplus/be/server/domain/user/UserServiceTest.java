package kr.hhplus.be.server.domain.user;

import kr.hhplus.be.server.infrastructure.user.FakeUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserServiceTest {

    FakeUserRepository userRepository = new FakeUserRepository();
    UserService userService = new UserService(userRepository);

    Long userId = 1L;
    String name = "test";
    Integer initPoint = 5000;
    UserCommand.Find userFindCommand = new UserCommand.Find(userId);

    @Nested
    @DisplayName("사용자 조회 테스트")
    class find_user_test{

        @BeforeEach
        void setUp() {
            userRepository.insertOrUpdate(new User(userId, name, initPoint));
        }

        @Test
        void 사용자_고유번호로_상세정보를_조회한다(){
            assertEquals(name, userService.findById(userFindCommand).name());
        }

        @Test
        void 사용자_고유번호로_사용자의_현재보유포인트를_조횐한다(){
            assertEquals(initPoint, userService.findPointById(userFindCommand));
        }

    }

    @Nested
    @DisplayName("사용자 포인트 충전 테스트")
    class charge_user_point_test{

        Integer validChargePoint = 15000;
        UserCommand.Charge chargeCommand = new UserCommand.Charge(userId, validChargePoint);

        @BeforeEach
        void setUp() {
            userRepository.insertOrUpdate(new User(userId, name, initPoint));
        }

        @Test
        void 사용자_포인트를_충전한다(){
            assertEquals(initPoint + validChargePoint, userService.charge(chargeCommand));
        }

    }

}
