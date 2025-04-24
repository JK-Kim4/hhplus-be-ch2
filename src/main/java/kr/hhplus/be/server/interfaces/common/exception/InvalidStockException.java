package kr.hhplus.be.server.interfaces.common.exception;

public class InvalidStockException extends RuntimeException {

    public static final String OVER_MAXIMUM_STOCK_QUANTITY = "상품 재고는 100,000개를 초과할 수 없습니다.";
    public static final String INSUFFICIENT_MINIMUM_STOCK_QUANTITY =  "최소 잔고 수량은 0개입니다.";
    public static final String INSUFFICIENT_STOCK_QUANTITY =  "주문 가능한 상품 재고가 부족합니다.";
    public static final String INVALID_DECREASE_QUANTITY =  "유효하지 않은 재고 차감 요청입니다.";
    public static final String INVALID_INCREASE_QUANTITY =  "유효하지 않은 재고 추가 요청입니다.";

    public InvalidStockException(String message) {
        this.message = message;
    }

    private String message;

    @Override
    public String getMessage() {
        return message;
    }
}
