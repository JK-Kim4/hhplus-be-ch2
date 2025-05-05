package kr.hhplus.be.server.domain.balance;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PointTest {

    @Test
    void 초기_포인트는_0이다() {
        Point point = new Point();
        assertEquals(BigDecimal.ZERO, point.getAmount());
    }

    @Test
    void of_메서드는_초기포인트를_전달받아_포인트를_생성한다() {
        Point point = Point.of(new BigDecimal("1000"));
        assertEquals(new BigDecimal("1000"), point.getAmount());
    }

    @Test
    void of_메서드는_음수이면_예외를_던진다() {
        assertThrows(IllegalArgumentException.class, () -> Point.of(new BigDecimal("-1000")));
    }

    @Test
    void charge_메서드는_포인트를_충전한다() {
        Point point = new Point();
        point.charge(new BigDecimal("500"));
        assertEquals(new BigDecimal("500"), point.getAmount());
    }

    @Test
    void charge_메서드는_음수이면_예외를_던진다() {
        Point point = new Point();
        assertThrows(IllegalArgumentException.class, () -> point.charge(new BigDecimal("-100")));
    }

    @Test
    void deduct_메서드는_포인트를_차감한다() {
        Point point = Point.of(new BigDecimal("1000"));
        point.deduct(new BigDecimal("300"));
        assertEquals(new BigDecimal("700"), point.getAmount());
    }

    @Test
    void deduct_메서드는_음수이면_예외를_던진다() {
        Point point = Point.of(new BigDecimal("1000"));
        assertThrows(IllegalArgumentException.class, () -> point.deduct(new BigDecimal("-300")));
    }

    @Test
    void deduct_메서드는_잔액보다_많이_차감하면_예외를_던진다() {
        Point point = Point.of(new BigDecimal("500"));
        assertThrows(IllegalStateException.class, () -> point.deduct(new BigDecimal("600")));
    }
}
