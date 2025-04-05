package kr.hhplus.be.server.domain.order;

public enum OrderStatus {
    // 주문 접수
    ORDERED,
    // 결제 완료
    PAYMENT_COMPLETED,
    // 상품 준비 중
    PREPARING_PRODUCT,
    // 배송 시작
    SHIPPED,
    // 배송 완료
    DELIVERED,
    // 주문 취소
    CANCELLED,
    // 주문 환불
    REFUNDED
}
