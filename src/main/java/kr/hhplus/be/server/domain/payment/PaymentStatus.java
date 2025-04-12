package kr.hhplus.be.server.domain.payment;

public enum PaymentStatus {
    PAYMENT_PENDING("결제 대기"),
    PAYMENT_PROCESSING("결제 진행 중"),
    PAYMENT_COMPLETED("결제 완료"),
    PAYMENT_FAILED("결제 실패"),
    PAYMENT_CANCELLED("결제 취소"),
    PAYMENT_REFUNDED("환불 완료");

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
