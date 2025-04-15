package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.order.OrderUser;
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.user.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PaymentTest {

    @Test
    void 결제_객체를_생성합니다(){
        User user = mock(User.class);
        Order order = mock(Order.class);

        when(user.getId()).thenReturn(1L);
        when(order.getUserId()).thenReturn(1L);
        when(order.getId()).thenReturn(2L);

        Payment payment = new Payment(order, user);


        assertEquals(1L, payment.getOrderUser().getUser().getId());
        assertEquals(2L, payment.getOrderUser().getOrder().getId());
    }

    @Test
    void 결제를_생성하는_주문에대한_사용자정보가_일치하지않으면_오류를_발생한다(){
        User user = mock(User.class);
        Order order = mock(Order.class);

        when(user.getId()).thenReturn(1L);
        when(order.getUserId()).thenReturn(2L);

        assertThrows(IllegalArgumentException.class,
                () -> new Payment(order, user));
    }

    @Test
    void 결제_요청시점_응답시점을_기록한다(){
        LocalDateTime requestTime = LocalDateTime.of(2024,1,1,1,1);
        LocalDateTime responseTime = LocalDateTime.of(2025,2,2,2,2);
        User user = mock(User.class);
        Order order = mock(Order.class);

        Payment payment = new Payment(order, user);
        payment.logRequestDateTime(requestTime);
        payment.logResponseDateTime(responseTime);

        assertEquals(requestTime, payment.getPaymentRequestDateTime());
        assertEquals(responseTime, payment.getPaymentResponseDateTime());
    }

    @Test
    void 사용자잔고가_주문금액보다_부족할경우_오류가발생한다(){
        User user = mock(User.class);
        Order order = mock(Order.class);
        Point userPoint = mock(Point.class);

        when(user.getPoint()).thenReturn(userPoint);
        when(userPoint.getAmount()).thenReturn(5000);
        when(order.getFinalPaymentPrice()).thenReturn(6000);

        Payment payment = new Payment(order, user);

        assertThrows(IllegalArgumentException.class,
                () -> payment.isPayable());
    }

    @Test
    void 잔액이_충분할경우_주문을_진행할수있다(){
        User user = mock(User.class);
        Order order = mock(Order.class);
        Point userPoint = mock(Point.class);

        when(user.getPoint()).thenReturn(userPoint);
        when(userPoint.getAmount()).thenReturn(7000);
        when(order.getFinalPaymentPrice()).thenReturn(6000);

        Payment payment = new Payment(order, user);

        assertTrue(payment.isPayable());
    }

    @Test
    void pay_ShouldCompletePaymentSuccessfully() {
        // given
        User user = mock(User.class);
        Order order = mock(Order.class);

        when(user.getId()).thenReturn(1L);
        when(order.getUserId()).thenReturn(1L);
        when(order.getFinalPaymentPrice()).thenReturn(1000);
        Payment payment = new Payment(order, user);

        OrderUser orderUser = mock(OrderUser.class);
        payment = spy(payment);

        // mocking 내부 OrderUser 호출들
        doReturn(orderUser).when(payment).getOrder();
        doNothing().when(orderUser).deductOrderItemStock();
        doNothing().when(orderUser).deductPrice(anyInt());
        doNothing().when(orderUser).updateOrderStatus(any(OrderStatus.class));
        doReturn(true).when(orderUser).hasEnoughPoint();
        payment.updatePaymentStatus(PaymentStatus.PAYMENT_PENDING);

        // when
        boolean payable = payment.isPayable();
        assertTrue(payable);

        payment.pay();

        // then
        assertEquals(PaymentStatus.PAYMENT_COMPLETED, payment.getPaymentStatus());
        assertNotNull(payment.getPaymentRequestDateTime());
        assertNotNull(payment.getPaymentResponseDateTime());
    }

    @Test
    void pay_ShouldThrowException_WhenNotPayable() {
        // given
        User user = mock(User.class);
        Order order = mock(Order.class);
        Payment payment = new Payment(order, user);
        payment.updatePaymentStatus(PaymentStatus.PAYMENT_COMPLETED); // 이미 완료 상태로 설정

        // when & then
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, payment::isPayable);
        assertEquals("진행할 수 없는 결제입니다.", ex.getMessage());
    }


}
