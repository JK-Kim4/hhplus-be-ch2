package kr.hhplus.be.server.domain.point;

import kr.hhplus.be.server.interfaces.exception.InvalidAmountException;

public class Point {

    public static final Integer MINIMUM_CHARGE_AMOUNT = 100;
    public static final Integer MAXIMUM_BALANCE = 100_000_000;

    private Long id;
    private Long userId;
    private Integer amount;
    private PointHistories pointHistories;

    private Point() {}

    private Point(Long userId, Integer amount) {
        this.userId = userId;
        this.amount = amount;
    }

    public static Point create(Long userId) {

        if(userId == null) {
            throw new IllegalArgumentException("사용자 정보를 전달해주세요.");
        }

        return new Point(userId, 0);
    }

    public void charge(Integer chargeAmount) {
        if(chargeAmount < MINIMUM_CHARGE_AMOUNT) {
            throw new InvalidAmountException(InvalidAmountException.INSUFFICIENT_CHARGE_AMOUNT);
        }

        if(this.amount + chargeAmount > MAXIMUM_BALANCE) {
            throw new InvalidAmountException(InvalidAmountException.MAXIMUM_BALANCE_EXCEEDED);
        }

        this.amount += chargeAmount;
    }

    public void deduct(Integer deductAmount) {
        if(this.amount < deductAmount){
            throw new InvalidAmountException(InvalidAmountException.INSUFFICIENT_BALANCE);
        }

        this.amount -= deductAmount;
    }

    public Integer getAmount() {
        return this.amount;
    }

    public Long getUserId() {
        return userId;
    }

}
