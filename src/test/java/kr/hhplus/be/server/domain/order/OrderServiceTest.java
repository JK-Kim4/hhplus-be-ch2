package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.item.ItemRepository;
import kr.hhplus.be.server.domain.order.command.OrderCommand;
import kr.hhplus.be.server.domain.order.command.OrderInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    private OrderRepository orderRepository;
    private OrderService orderService;
    private ItemRepository itemRepository;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        itemRepository = mock(ItemRepository.class);
        orderService = new OrderService(orderRepository, itemRepository);
    }

    @Test
    void testCreateOrder_success() {
        // given
        OrderCommand.OrderItemCreate item1 = mock(OrderCommand.OrderItemCreate.class);
        OrderCommand.OrderItemCreate item2 = mock(OrderCommand.OrderItemCreate.class);
        List<OrderCommand.OrderItemCreate> commandItems = List.of(item1, item2);
        OrderCommand.Create command = mock(OrderCommand.Create.class);
        when(command.getOrderItems()).thenReturn(commandItems);

        OrderItem orderItemEntity1 = mock(OrderItem.class);
        OrderItem orderItemEntity2 = mock(OrderItem.class);
        when(item1.toEntity()).thenReturn(orderItemEntity1);
        when(item2.toEntity()).thenReturn(orderItemEntity2);

        List<OrderItem> itemEntities = List.of(orderItemEntity1, orderItemEntity2);
        Order orderEntity = mock(Order.class);
        when(command.toEntity(itemEntities)).thenReturn(orderEntity);
        when(orderRepository.save(any(Order.class))).thenReturn(orderEntity);

        // when
        OrderInfo.Create result = orderService.createOrder(command);

        // then
        verify(orderRepository).save(orderEntity);
        verify(orderEntity).registerOrderItems(any(OrderItems.class));
        assertNotNull(result);
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
