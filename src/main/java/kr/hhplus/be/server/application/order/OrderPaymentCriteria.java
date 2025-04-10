package kr.hhplus.be.server.application.order;

import java.util.List;

public class OrderPaymentCriteria {

    private Long userId;
    private Long userCouponId;
    private List<OrderItemCriteria> orderItems;

    public Long getUserId() {
        return userId;
    }

    public Long getUserCouponId() {
        return userCouponId;
    }

    public List<OrderItemCriteria> getOrderItems() {
        return orderItems;
    }
}
