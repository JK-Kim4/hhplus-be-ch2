package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderUser {

    private Order order;
    private User user;

    public OrderUser(Order order, User user) {
        this.order = order;
        this.user = user;
    }

    public boolean hasEnoughPoint(){
        if(user.point() < order.getFinalPaymentPrice()){
            throw new IllegalArgumentException("잔액이 부족합니다.");
        }

        return true;
    }

    public void deductPrice(Integer price) {
        if(!order.getFinalPaymentPrice().equals(price)){
            throw new IllegalArgumentException("결제 금액이 올바르지않습니다.");
        }
        user.deductPoint(price);
    }

    public void deductOrderItemStock() {
        order.deductOrderItemStock();
    }

    public void updateOrderStatus(OrderStatus orderStatus) {
        this.order.updateOrderStatus(orderStatus);
    }


}
