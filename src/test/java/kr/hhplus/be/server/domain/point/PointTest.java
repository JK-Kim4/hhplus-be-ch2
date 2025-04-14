package kr.hhplus.be.server.domain.point;

import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.interfaces.exception.InvalidAmountException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class PointTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = mock(User.class);
    }

    @Test
    @DisplayName("사용자 정보가 null일 경우 예외 발생")
    void createPointWithNullUserThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Point.create(null);
        });

        assertEquals("사용자 정보를 전달해주세요.", exception.getMessage());
    }

    @Test
    @DisplayName("정상적인 사용자 정보로 Point 객체 생성")
    void createPointWithValidUser() {
        Point point = Point.create(user.getId());

        assertNotNull(point);
        assertEquals(0, point.getAmount());
    }

    @Test
    @DisplayName("충전 금액이 최소 금액보다 작으면 예외 발생")
    void chargeWithInvalidAmountThrowsException() {
        Point point = Point.create(user.getId());

        InvalidAmountException exception = assertThrows(InvalidAmountException.class, () -> {
            point.charge(50);
        });

        assertEquals(InvalidAmountException.INSUFFICIENT_CHARGE_AMOUNT, exception.getMessage());
    }

    @Test
    @DisplayName("충전 시 최대 잔액 초과하면 예외 발생")
    void chargeExceedingMaximumBalanceThrowsException() {
        Point point = Point.create(user.getId());
        point.charge(Point.MAXIMUM_BALANCE - 50); // 현재 잔액: MAX-50

        InvalidAmountException exception = assertThrows(InvalidAmountException.class, () -> {
            point.charge(100); // 초과
        });

        assertEquals(InvalidAmountException.MAXIMUM_BALANCE_EXCEEDED, exception.getMessage());
    }

    @Test
    @DisplayName("정상 충전 시 잔액 증가")
    void chargeIncreasesBalance() {
        Point point = Point.create(user.getId());
        point.charge(500);

        assertEquals(500, point.getAmount());
    }

    @Test
    @DisplayName("차감 금액이 잔액 초과 시 예외 발생")
    void deductWithInsufficientBalanceThrowsException() {
        Point point = Point.create(user.getId());
        point.charge(300);

        InvalidAmountException exception = assertThrows(InvalidAmountException.class, () -> {
            point.deduct(500);
        });

        assertEquals(InvalidAmountException.INSUFFICIENT_BALANCE, exception.getMessage());
    }

    @Test
    @DisplayName("정상 차감 시 잔액 감소")
    void deductDecreasesBalance() {
        Point point = Point.create(user.getId());
        point.charge(1000);
        point.deduct(400);

        assertEquals(600, point.getAmount());
    }
}
