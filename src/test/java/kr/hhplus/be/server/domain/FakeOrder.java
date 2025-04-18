package kr.hhplus.be.server.domain;

import kr.hhplus.be.server.domain.order.Order;

public class FakeOrder extends Order {


    public FakeOrder(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

}
