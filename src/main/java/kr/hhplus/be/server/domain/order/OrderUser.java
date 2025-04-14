package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.user.User;

public class OrderUser {

    private Order order;
    private User user;

    public OrderUser(Order order, User user) {
        this.order = order;
        this.user = user;
    }

    public Order getOrder() {
        return order;
    }

    public User getUser() {
        return user;
    }

    public void deductPrice(Integer price) {
        if(!order.getFinalPaymentPrice().equals(price)){
            throw new IllegalArgumentException("결제 금액이 올바르지않습니다.");
        }
        user.deductPoint(price);
    }

    public void updateOrderStatus(OrderStatus orderStatus) {
        this.order.updateOrderStatus(orderStatus);
    }


}
