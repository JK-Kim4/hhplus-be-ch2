package kr.hhplus.be.server.domain.user;

import kr.hhplus.be.server.interfaces.exception.InvalidAmountException;

import java.util.Objects;

public record UserPoint(Integer point) {

    public static final Integer MINIMUM_CHARGE_AMOUNT = 100;
    public static final Integer MAXIMUM_BALANCE = 100_000_000;


    public static UserPoint createOrDefault(Integer point) {
        return new UserPoint(Objects.requireNonNullElse(point, 0));

    }

    public Integer getPoint() {
        return this.point;
    }

    public UserPoint charge(Integer chargePoint) {
        if(chargePoint < MINIMUM_CHARGE_AMOUNT) {
            throw new InvalidAmountException(InvalidAmountException.INSUFFICIENT_CHARGE_AMOUNT);
        }

        if(this.point + chargePoint > MAXIMUM_BALANCE) {
            throw new InvalidAmountException(InvalidAmountException.MAXIMUM_BALANCE_EXCEEDED);
        }

        return new UserPoint(this.point + chargePoint);
    }

    public UserPoint deduct(Integer deductPoint) {
        if(this.point < deductPoint){
            throw new InvalidAmountException(InvalidAmountException.INSUFFICIENT_BALANCE);
        }

        return new UserPoint(this.point - deductPoint);
    }
}
