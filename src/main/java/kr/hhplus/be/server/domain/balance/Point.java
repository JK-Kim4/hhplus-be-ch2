package kr.hhplus.be.server.domain.balance;

import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

@Embeddable
public class Point {

    private BigDecimal amount;

    protected Point() {
        this.amount = BigDecimal.ZERO;
    }

    public static Point of(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("포인트는 0 이상이어야 합니다.");
        }
        return new Point(amount);
    }

    private Point(BigDecimal amount) {
        this.amount = amount;
    }

    public static Point createDefault() {
        return new Point(BigDecimal.ZERO);
    }

    public static Point create(BigDecimal point) {
        if(point == null){
            return createDefault();
        }

        return Point.of(point);
    }

    public void charge(BigDecimal value) {
        validateNonNegative(value);
        this.amount = this.amount.add(value);
    }

    public void deduct(BigDecimal value) {
        validateNonNegative(value);
        if (this.amount.compareTo(value) < 0) {
            throw new IllegalStateException("잔여 포인트가 부족합니다.");
        }
        this.amount = this.amount.subtract(value);
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    private void validateNonNegative(BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("포인트 값은 null이거나 음수일 수 없습니다.");
        }
    }

    @Override
    public String toString() {
        return amount.toPlainString();
    }
}
