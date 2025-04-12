package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.domain.item.Item;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.order.orderItem.OrderItem;
import kr.hhplus.be.server.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentTest {

    @Test
    @DisplayName("주문을 전달받아 결제를 생성하고 주문 상태를 결제 대기로 변경한다.")
    void create_payment_test(){
        //given
        User user = new User(null,"test", 500);
        Order order = new Order(user);

        //when
        Payment payment = new Payment(order);

        //then
        assertEquals(OrderStatus.PAYMENT_WAITING, payment.getOrder().getOrderStatus());
    }

    @Test
    @DisplayName("주문에 대한 결제를 진행하고 사용자 잔고를 차감한다.")
    void pay_payment_test(){
        //given
        User user = new User(null,"test", 100_000);
        Order order = new Order(user);
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(new OrderItem(order, new Item("test1", 5000, 50), 1));
        orderItems.add(new OrderItem(order, new Item("test2", 4000, 50), 1));
        orderItems.add(new OrderItem(order, new Item("test3", 3000, 50), 1));
        order.calculateTotalPrice(orderItems);
        Payment payment = new Payment(order);

        //when
        payment.pay();

        //then
        assertEquals((100_000 - 5000 - 4000 - 3000), payment.getOrder().getOrderUser().getPoint());
    }
}
