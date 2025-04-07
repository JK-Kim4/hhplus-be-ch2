package kr.hhplus.be.server.interfaces.exception;

public class InvalidPriceException extends RuntimeException {

    public static final String INSUFFICIENT_MINIMUM_PRICE = "최소 판매가능 가격은 100원입니다.";
    public static final String OVER_MAXIMUM_PRICE = "최대 판매 가능 금액은 100,000,000원입니다.";

    private String message;

    public InvalidPriceException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
