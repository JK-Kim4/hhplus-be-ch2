package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.FakeUser;
import kr.hhplus.be.server.domain.item.Item;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderTest {

    private OrderItem orderItem1;
    private OrderItem orderItem2;
    private OrderItems orderItems;
    private List<OrderItem> itemList;

    @BeforeEach
    void setUp() {
        orderItem1 = mock(OrderItem.class);
        orderItem2 = mock(OrderItem.class);
        itemList = List.of(orderItem1, orderItem2);

        orderItems = mock(OrderItems.class);
        when(orderItems.calculateTotalPrice()).thenReturn(2000);
    }

    @Test
    void testOrderCreation() {
        User user = mock(User.class);
        Order order = new Order(user, itemList);

        assertEquals(2, order.getOrderItems().size());
        assertEquals(OrderStatus.ORDER_CREATED, order.getOrderStatus());
    }

    @Test
    void testCalculateTotalPrice() {
        User user = mock(User.class);
        OrderItem orderItem1 = new OrderItem(mock(Item.class),1000, 50);
        OrderItem orderItem2 = new OrderItem(mock(Item.class),2000, 50);
        OrderItem orderItem3 = new OrderItem(mock(Item.class), 3000, 50);
        List<OrderItem> orderItemList = List.of(orderItem1, orderItem2, orderItem3);

        Order order = new Order(user, orderItemList);

        order.calculateTotalPrice();

        assertEquals(OrderStatus.ORDER_CREATED, order.getOrderStatus());
        assertEquals(orderItem1.calculatePrice() + orderItem2.calculatePrice(), order.getTotalPrice(),
                order.getTotalPrice());
    }

    @Test
    void testRegisterPayment() {
        Order order = new Order(new FakeUser(2L, "tesr"), itemList);
        Payment payment = mock(Payment.class);

        order.registerPayment(payment);

        assertEquals(payment, order.getPayment());
    }

    @Test
    void testUpdateOrderStatus() {
        Order order = new Order(new FakeUser(1L, "test"), itemList);

        order.updateOrderStatus(OrderStatus.PAYMENT_WAITING);

        assertEquals(OrderStatus.PAYMENT_WAITING, order.getOrderStatus());
    }


}
