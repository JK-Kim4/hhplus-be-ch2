package kr.hhplus.be.server.domain.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    private OrderRepository orderRepository;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        orderService = new OrderService(orderRepository);
    }

    @Test
    void testFindOrdersByDateAndStatus_success() {
        // given
        LocalDate date = LocalDate.now();
        OrderStatus status = OrderStatus.PAYMENT_COMPLETED;
        List<Order> mockOrders = List.of(mock(Order.class), mock(Order.class));

        when(orderRepository.findByDateAndStatus(date, status)).thenReturn(mockOrders);

        // when
        List<Order> result = orderService.findOrdersByDateAndStatus(date, status);

        // then
        assertEquals(mockOrders, result);
        verify(orderRepository).findByDateAndStatus(date, status);
    }

    @Test
    void testFindOrderItemsByOrderIds_success() {
        // given
        Order order1 = mock(Order.class);
        Order order2 = mock(Order.class);
        when(order1.getId()).thenReturn(1L);
        when(order2.getId()).thenReturn(2L);

        List<Order> orders = List.of(order1, order2);
        List<OrderItem> mockOrderItems = List.of(mock(OrderItem.class));

        when(orderRepository.findOrderItemsByOrderIds(List.of(1L, 2L))).thenReturn(mockOrderItems);

        // when
        List<OrderItem> result = orderService.findOrderItemsByOrderIds(orders);

        // then
        assertEquals(mockOrderItems, result);
        verify(orderRepository).findOrderItemsByOrderIds(List.of(1L, 2L));
    }



}
