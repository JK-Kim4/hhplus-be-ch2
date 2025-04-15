package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.userCoupon.UserCoupon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

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
        Order order = new Order(user.getId(), null, itemList);

        assertEquals(user.getId(), order.getUserId());
        assertEquals(2, order.getOrderItems().size());
        assertEquals(OrderStatus.ORDER_CREATED, order.getOrderStatus());
    }

    @Test
    void testCalculateTotalPrice() {
        OrderItem orderItem1 = OrderItem.of(1L, 1000, 50);
        OrderItem orderItem2 = OrderItem.of(2L, 2000, 50);
        OrderItem orderItem3 = OrderItem.of(3L, 3000, 50);
        List<OrderItem> orderItemList = List.of(orderItem1, orderItem2, orderItem3);

        Order order = new Order(1L, 2L, orderItemList);

        order.calculateTotalPrice();

        assertEquals(OrderStatus.ORDER_CREATED, order.getOrderStatus());
        assertEquals(orderItem1.calculatePrice() + orderItem2.calculatePrice(), order.getTotalPrice(),
                order.getTotalPrice());
    }

    @Test
    void testApplyCoupon_success() {
        Order order = new Order(1L, null, itemList);
        order.calculateTotalPrice(); // assuming total = 0 now

        UserCoupon coupon = mock(UserCoupon.class);
        when(coupon.isCouponOwner(1L)).thenReturn(true);
        when(coupon.getId()).thenReturn(99L);
        when(coupon.discount(anyInt())).thenReturn(1500);

        order.calculateTotalPrice(); // to set totalPrice
        order.applyCoupon(coupon);

        assertEquals(99L, order.getUserCouponId());
        assertEquals(1500, order.getFinalPaymentPrice());
        verify(coupon).updateUsedFlag(true);
    }

    @Test
    void testRegisterPayment() {
        Order order = new Order(1L, null, itemList);
        Payment payment = mock(Payment.class);

        order.registerPayment(payment);

        assertEquals(payment, order.getPayment());
    }

    @Test
    void testUpdateOrderStatus() {
        Order order = new Order(1L, null, itemList);

        order.updateOrderStatus(OrderStatus.PAYMENT_WAITING);

        assertEquals(OrderStatus.PAYMENT_WAITING, order.getOrderStatus());
    }

    @Test
    void testRegisterOrderItems() {
        Order order = new Order(1L, null, itemList);
        OrderItems newOrderItems = mock(OrderItems.class);

        order.registerOrderItems(newOrderItems);

        assertEquals(newOrderItems, order.getOrderItems());
        verify(newOrderItems).setOrder(order);
    }

    @Test
    void testConstructorWithUser() {
        User user = mock(User.class);
        when(user.getId()).thenReturn(123L);

        Order order = new Order(user);

        assertEquals(123L, order.getUserId());
    }


}
