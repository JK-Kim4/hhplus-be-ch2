package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.application.order.OrderItemCriteria;
import kr.hhplus.be.server.application.order.OrderPaymentCriteria;
import kr.hhplus.be.server.domain.order.command.OrderCreateCommand;
import kr.hhplus.be.server.domain.order.command.OrderItemCreateCommand;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class OrderCommandTest {


    @Test
    void constructor_setsFieldsCorrectly() {
        Long userId = 1L;
        Long userCouponId = 10L;
        List<OrderItemCreateCommand> items = List.of(new OrderItemCreateCommand(101L, 1000, 5));

        OrderCreateCommand command = new OrderCreateCommand(userId, userCouponId, items);

        assertEquals(userId, command.getUserId());
        assertEquals(userCouponId, command.getUserCouponId());
        assertEquals(items, command.getOrderItems());
    }

    @Test
    void from_createsCommandFromCriteria() {
        // Given
        OrderPaymentCriteria criteria = mock(OrderPaymentCriteria.class);
        when(criteria.getUserId()).thenReturn(1L);
        when(criteria.getOrderItems()).thenReturn(List.of(
                mock(OrderItemCriteria.class)
        ));

        // And: mock OrderItemCreateCommand.from()
        mockStatic(OrderItemCreateCommand.class);
        OrderItemCreateCommand mockItemCommand = new OrderItemCreateCommand(101L, 1000, 5);
        when(OrderItemCreateCommand.from(any())).thenReturn(mockItemCommand);

        // When
        OrderCreateCommand command = OrderCreateCommand.from(criteria);

        // Then
        assertNotNull(command);
        assertEquals(criteria.getUserId(), command.getUserId());
        assertEquals(1, command.getOrderItems().size());
        assertEquals(mockItemCommand, command.getOrderItems().get(0));
    }

    @Test
    void responseWrapper_wrapsOrderCorrectly() {
        Order order = mock(Order.class);

        OrderCreateCommand.Response response = new OrderCreateCommand.Response(order);

        assertNotNull(response.getOrder());
        assertEquals(order, response.getOrder());
    }
}
