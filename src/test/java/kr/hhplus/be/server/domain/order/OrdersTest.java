package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.product.Price;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

public class OrdersTest {

    @Test
    void 사용자_주문목록중_주문상태가_ORDER_CREATED상태_주문이_존재할경우_오류반환(){
        //given
        Order order1 = Order.create(1L, List.of(OrderItem.create(1L, Price.of(BigDecimal.valueOf(5000)), 1)));
        order1.completePayment();
        Order order2 = Order.create(2L, List.of(OrderItem.create(1L, Price.of(BigDecimal.valueOf(5000)), 1)));

        Assertions.assertThrows(IllegalStateException.class, () -> Orders.validateNoPendingOrders(List.of(order1, order2)));
    }
}
