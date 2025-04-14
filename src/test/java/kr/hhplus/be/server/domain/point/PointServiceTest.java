package kr.hhplus.be.server.domain.point;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PointServiceTest {

    private PointRepository pointRepository;
    private PointService pointService;

    @BeforeEach
    void setUp() {
        pointRepository = mock(PointRepository.class);
        pointService = new PointService(pointRepository);
    }

    @Test
    @DisplayName("기존 포인트가 있으면 해당 객체에 금액 충전")
    void charge_existingPoint() {
        Long userId = 1L;
        PointCommand.Charge command = PointCommand.Charge.of(userId, 1000);
        Point mockPoint = mock(Point.class);

        when(pointRepository.findByUserId(userId)).thenReturn(Optional.of(mockPoint));

        pointService.charge(command);

        verify(mockPoint, times(1)).charge(1000);
        verify(pointRepository, never()).save(any());
    }

    @Test
    @DisplayName("포인트가 없으면 새로 만들고 충전 및 저장")
    void charge_newPointCreated() {
        Long userId = 2L;
        PointCommand.Charge command = PointCommand.Charge.of(userId, 5000);

        when(pointRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // 실제 객체로 동작을 테스트
        ArgumentCaptor<Point> captor = ArgumentCaptor.forClass(Point.class);
        doNothing().when(pointRepository).save(captor.capture());

        pointService.charge(command);

        Point savedPoint = captor.getValue();
        assertNotNull(savedPoint);
        assertEquals(userId, savedPoint.getUserId());
        // charge 이후 amount가 5000인지 확인
        assertEquals(5000, savedPoint.getAmount());
    }

    @Test
    @DisplayName("getPoint는 PointInfo.Point를 반환한다")
    void getPoint_returnsPointInfo() {
        Long userId = 3L;
        Point mockPoint = mock(Point.class);
        when(mockPoint.getUserId()).thenReturn(userId);
        when(mockPoint.getAmount()).thenReturn(7000);

        when(pointRepository.findByUserId(userId)).thenReturn(Optional.of(mockPoint));

        PointInfo.Point result = pointService.getPoint(userId);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(7000, result.getAmount());
    }

}
