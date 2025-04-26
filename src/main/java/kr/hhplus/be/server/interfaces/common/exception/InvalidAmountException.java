package kr.hhplus.be.server.interfaces.common.exception;

public class InvalidAmountException extends RuntimeException {

    public static final String INSUFFICIENT_CHARGE_AMOUNT = "최소 충전 금액은 100원입니다.";
    public static final String MAXIMUM_BALANCE_EXCEEDED = "최대 보유 가능 금액은 100,000,000원입니다.";
    public static final String INSUFFICIENT_BALANCE = "잔액이 부족합니다.";
    public static final String INVALID_POINT_ARGUMENT = "잔액은 0이상이어야합니다.";

    public InvalidAmountException(String message) {
        this.message = message;
    }

    public String message;

    public String getMessage() {
        return this.message;
    }


}
