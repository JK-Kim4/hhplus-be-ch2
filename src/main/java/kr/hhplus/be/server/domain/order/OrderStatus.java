package kr.hhplus.be.server.domain.order;

public enum OrderStatus {
    ORDER_CREATED("주문 생성"),
    PAYMENT_WAITING("결제 대기"),
    PAYMENT_COMPLETED("결제 완료");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
