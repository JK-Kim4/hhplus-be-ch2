package kr.hhplus.be.server.domain.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    Long userId = 1L;
    Integer initPoint = 5000;

    @Nested
    @DisplayName("사용자 조회 테스트")
    class find_user_test{

        UserCommand.Find userFindCommand = new UserCommand.Find(userId);

        @BeforeEach
        void setUp() {
            User user = new User(userId, "test", initPoint);
            Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        }

        @Test
        void 사용자_고유번호로_상세정보를_조회한다(){
            assertEquals("test", userService.findById(userFindCommand).name());
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
            User user = new User(userId, "test", initPoint);
            Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        }

        @Test
        void 사용자_포인트를_충전한다(){
            assertEquals(initPoint + validChargePoint, userService.charge(chargeCommand));
        }

    }

}
