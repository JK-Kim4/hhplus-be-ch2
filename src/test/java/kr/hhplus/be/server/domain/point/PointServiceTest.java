package kr.hhplus.be.server.domain.point;

import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PointServiceTest {

    @Mock
    private PointRepository pointRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PointService pointService;

    @Test
    @DisplayName("기존 포인트가 있으면 해당 객체에 금액 충전")
    void charge_existingPoint() {
        Long userId = 1L;
        PointCommand.Charge command = PointCommand.Charge.of(userId, 1000);
        Point mockPoint = mock(Point.class);

        when(pointRepository.findByUserId(userId)).thenReturn(Optional.of(mockPoint));
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User(2L, "user")));

        pointService.charge(command);

        verify(mockPoint, times(1)).charge(1000);
    }

    @Test
    @DisplayName("포인트가 없으면 새로 만들고 충전 및 저장")
    void charge_newPointCreated() {
        Long userId = 2L;
        PointCommand.Charge command = PointCommand.Charge.of(userId, 5000);

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User(2L, "user")));

        // 실제 객체로 동작을 테스트
        ArgumentCaptor<Point> captor = ArgumentCaptor.forClass(Point.class);
        doNothing().when(pointRepository).save(captor.capture());

        pointService.charge(command);

        Point savedPoint = captor.getValue();
        assertNotNull(savedPoint);
        assertEquals(userId, savedPoint.getUser().getId());
        // charge 이후 amount가 5000인지 확인
        assertEquals(5000, savedPoint.getAmount());
    }

}
