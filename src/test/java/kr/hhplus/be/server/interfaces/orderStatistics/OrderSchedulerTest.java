package kr.hhplus.be.server.interfaces.orderStatistics;

import kr.hhplus.be.server.domain.item.Item;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.orderStatistics.OrderStatistics;
import kr.hhplus.be.server.domain.orderStatistics.OrderStatisticsService;
import kr.hhplus.be.server.interfaces.scheduler.OrderStatisticScheduler;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderSchedulerTest {

    @Mock
    private OrderService orderService;

    @Mock
    private OrderStatisticsService orderStatisticsService;

    @InjectMocks
    private OrderStatisticScheduler orderScheduler;


    @Test
    @Disabled
    @DisplayName("상품 판매 통계 스케줄러 테스트")
    void orderStatistic_shouldCalculateAndSaveStatistics() {
        // given
        Order order1 = new Order(null, null);
        Order order2 = new Order(null, null);
        Order order3 = new Order(null, null);
        Item car = Item.createWithNameAndPriceAndStock("car", 1000, 50);
        Item desk = Item.createWithNameAndPriceAndStock("desk", 5000, 50);
        Item food = Item.createWithNameAndPriceAndStock("food", 3000, 50);
        when(car.getId()).thenReturn(1L);
        when(desk.getId()).thenReturn(2L);
        when(food.getId()).thenReturn(3L);
        LocalDate targetDate = LocalDate.now().minusDays(1);
        List<Order> mockOrders = List.of(order1,order2,order3);

        List<OrderItem> mockOrderItems = List.of(
                new OrderItem(car, 1000, 20),
                new OrderItem(desk, 5000, 20),
                new OrderItem(food, 3000, 20)
        );

        List<OrderStatistics> mockStatistics = OrderStatistics.calculateOrderStatistics(mockOrderItems, targetDate);

        // when
        when(orderService.findOrdersByDateAndStatus(targetDate, OrderStatus.PAYMENT_COMPLETED)).thenReturn(mockOrders);
        when(orderService.findOrderItemsByOrderIds(mockOrders)).thenReturn(mockOrderItems);

        // execute
        orderScheduler.orderStatistic();

        // then
        verify(orderService).findOrdersByDateAndStatus(targetDate, OrderStatus.PAYMENT_COMPLETED);
        verify(orderService).findOrderItemsByOrderIds(mockOrders);
        verify(orderStatisticsService, times(1)).save(any());
    }

}
