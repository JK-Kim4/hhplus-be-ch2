package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentTest {

    //TODO
    @Test
    void 주문과_사용자정보를_받아_결제를_생성한다(){
        User user = mock(User.class);
        Order order = mock(Order.class);
        when(order.getUser()).thenReturn(user);

        new Payment(order, user);
    }

    @Test
    void 주문사용자정보와_파라미터사용자정보가_일치하지않을경우_결제생성에_실패한다(){
        User user = mock(User.class);
        Order order = mock(Order.class);
        when(order.getUser()).thenReturn(mock(User.class));

        assertThrows(IllegalArgumentException.class,
                () -> new Payment(order, user));
    }

    @Test
    void 사용자_잔고가_결제금액보다_많을경우_결제가_가능하다(){
        User user = mock(User.class);
        Order order = mock(Order.class);
        when(order.getUser()).thenReturn(user);
        when(order.getFinalPaymentPrice()).thenReturn(10_000);
        when(user.point()).thenReturn(50_000);

        Payment payment = new Payment(order, user);

        assertTrue(payment.isPayable(user));
    }

    @Test
    void 결제사용자정보와_파라미터사용자정보가_일치하지않을경우_결제가능여부를_확인할수없다(){
        User user = mock(User.class);
        Order order = mock(Order.class);
        when(order.getUser()).thenReturn(user);

        Payment payment = new Payment(order, user);

        assertThrows(IllegalArgumentException.class,
                () -> payment.isPayable(mock(User.class)));
    }

    @Test
    void 결제처리가_완료되면_상품재고와_사용자잔고가_차감된다(){
        User user = mock(User.class);
        Order order = mock(Order.class);
        when(order.getUser()).thenReturn(user);

        Payment payment = new Payment(order, user);

        payment.pay(user);

        verify(order, times(1)).deductOrderItemStock();
        verify(user, times(1)).deductPoint(any());
        verify(order, times(1)).updateOrderStatus(OrderStatus.PAYMENT_COMPLETED);
    }

}
