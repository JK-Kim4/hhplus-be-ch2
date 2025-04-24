package kr.hhplus.be.server.interfaces.common.exception;

public class InvalidPriceException extends RuntimeException {

    public static final String INSUFFICIENT_MINIMUM_PRICE = "최소 판매가능 가격은 100원입니다.";
    public static final String OVER_MAXIMUM_PRICE = "최대 판매 가능 금액은 100,000,000원입니다.";
    public static final String ITEM_PRICE_MISMATCH = "주문 상품의 가격 정보가 일치하지않습니다. [상품 고유번호: %d]";

    private String message;

    public InvalidPriceException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
