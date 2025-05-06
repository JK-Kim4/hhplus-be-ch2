package kr.hhplus.be.server.domain.order;

public enum OrderStatus {

    ORDER_CREATED, PAID;

    public boolean isPendingPayment() {
        return this == ORDER_CREATED;
    }
}
