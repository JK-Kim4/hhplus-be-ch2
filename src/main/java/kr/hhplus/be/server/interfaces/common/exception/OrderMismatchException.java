package kr.hhplus.be.server.interfaces.common.exception;

public class OrderMismatchException extends RuntimeException {

    public static final String DEFAULT = "주문 정보가 일치하지 않습니다.";

    private String message;

    public OrderMismatchException(){
        this.message = DEFAULT;
    }

    public OrderMismatchException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}

