package kr.hhplus.be.server.interfaces.exception;

public class NotEnoughStockException extends RuntimeException {

    public static final String NOT_ENOUGH_STOCK = "재고가 부족합니다.";


    private String message;

    public NotEnoughStockException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
