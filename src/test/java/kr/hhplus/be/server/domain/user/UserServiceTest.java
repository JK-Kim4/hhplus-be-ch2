package kr.hhplus.be.server.domain.user;

import kr.hhplus.be.server.domain.user.pointHistory.PointChargeCommand;
import kr.hhplus.be.server.domain.user.pointHistory.PointHistory;
import kr.hhplus.be.server.domain.user.pointHistory.PointHistoryRepository;
import kr.hhplus.be.server.domain.user.pointHistory.PointHistoryType;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PointHistoryRepository pointHistoryRepository;

    @InjectMocks
    UserService userService;

    Long userId = 1L;
    Integer initPoint = 5000;

    @Nested
    @DisplayName("사용자 조회 테스트")
    class find_user_test{

        @BeforeEach
        void setUp() {
            User user = new User(userId, "test", initPoint);
            Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        }

        @Test
        void 사용자_고유번호로_상세정보를_조회한다(){
            assertEquals("test", userService.findById(userId).getName());
        }

        @Test
        void 사용자_고유번호로_사용자의_현재보유포인트를_조횐한다(){
            assertEquals(initPoint, userService.findPointById(userId));
        }

    }

    @Nested
    @DisplayName("사용자 포인트 충전 테스트")
    class charge_user_point_test{

        Integer validChargePoint = 15000;
        PointChargeCommand chargeCommand = new PointChargeCommand(userId, validChargePoint);
        User user;
        PointHistory pointHistory;


        @BeforeEach
        void setUp() {
            user = new User(userId, "test", initPoint);
            pointHistory = new PointHistory(userId,  validChargePoint, PointHistoryType.CHARGE);
            Mockito.when(userRepository.findById(userId))
                    .thenReturn(Optional.of(user));
            Mockito.when(pointHistoryRepository.save(pointHistory))
                    .thenReturn(pointHistory);
        }

        @Test
        void 사용자_포인트를_충전하고_충전이력을_저장을_호출한다(){
            assertEquals(initPoint + validChargePoint, userService.charge(chargeCommand));
            verify(pointHistoryRepository, times(1)).save(pointHistory);
        }

    }

}
