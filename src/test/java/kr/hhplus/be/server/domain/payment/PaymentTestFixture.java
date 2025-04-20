package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderTestFixture;
import kr.hhplus.be.server.domain.user.User;

public class PaymentTestFixture {

    public static Payment createTestPayment(){
        Order order = OrderTestFixture.createTestOrder();
        User user = order.getUser();

        return new Payment(order, user);
    }

    public static Payment creatTestPaymentWithOrderAndUser(Order order){
        return new Payment(order, order.getUser());
    }
}
