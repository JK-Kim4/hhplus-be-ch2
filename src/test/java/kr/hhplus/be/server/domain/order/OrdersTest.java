package kr.hhplus.be.server.domain.order;


import kr.hhplus.be.server.domain.FakeUser;
import kr.hhplus.be.server.domain.user.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrdersTest {

    @Test
    void 결제가_완료되지않은_주문이_존재할경우_주문을_생성시_오류를반환한다(){
        User user = new FakeUser(1L, "User");
        user.chargePoint(10_000);

        Order completeOrder = mock(Order.class);
        when(completeOrder.getOrderStatus()).thenReturn(OrderStatus.PAYMENT_COMPLETED);
        Order notPaidOrder = mock(Order.class);
        when(notPaidOrder.getOrderStatus()).thenReturn(OrderStatus.PAYMENT_WAITING);

        user.addOrder(completeOrder);
        user.addOrder(notPaidOrder);

        assertThrows(IllegalArgumentException.class, () -> user.canCreateOrder());

    }


}
