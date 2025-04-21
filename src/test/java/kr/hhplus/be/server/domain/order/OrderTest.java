package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.payment.Payment;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {

    @Test
    void 주문생성시_ORDER_CREATE상태의_주문이_생성된다(){
        //when
        Order order = OrderTestFixture.createTestOrder();

        //then
        assertNotNull(order);
        assertEquals(OrderStatus.ORDER_CREATED, order.getOrderStatus());
    }

    @Test
    void 주문상품의_총금액을_계산한다(){
        //given
        Order order = OrderTestFixture.createTestOrder();

        //when
        order.calculateTotalPrice();

        //then
        assertEquals(100_000, order.getTotalPrice());
    }

    @Test
    void 주문의_상태정보를_변경한다(){
        //given
        Order order = OrderTestFixture.createTestOrder();

        //when
        order.updateOrderStatus(OrderStatus.PAYMENT_WAITING);

        //then
        assertEquals(OrderStatus.PAYMENT_WAITING, order.getOrderStatus());
    }

    @Test
    void 결제정보를_전달받아_registerPayment를_호출하면_주문상태를_PAYMENT_WAITING_변경하고_결제정보를_갱신한다(){
        //given
        Order order = OrderTestFixture.createTestOrder();
        Payment payment = new Payment(order, order.getUser());

        //when
        assertNull(order.getPayment());
        order.registerPayment(payment);

        //then
        assertNotNull(order.getPayment());
        assertEquals(payment, order.getPayment());
        assertEquals(OrderStatus.PAYMENT_WAITING, order.getOrderStatus());
    }



}
