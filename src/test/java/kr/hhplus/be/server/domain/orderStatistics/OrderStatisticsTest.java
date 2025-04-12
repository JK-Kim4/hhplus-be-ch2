package kr.hhplus.be.server.domain.orderStatistics;

import kr.hhplus.be.server.domain.item.Item;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.orderItem.OrderItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderStatisticsTest {

    @Test
    @DisplayName("주문 상품 목록을 전달받아 판매 통계 목록을 생성한다.")
    void calculate_orderStatistics_test() {
        Order order1 = new Order(1L, null, null);
        Order order2 = new Order(2L, null, null);
        Order order3 = new Order(3L, null, null);
        Item car = new Item(1L,"car", 1000, 50);
        Item desk = new Item(2L, "desk", 5000, 50);
        Item food = new Item(3L, "food", 3000, 50);

        List<OrderItem> orderItemList = List.of(
                new OrderItem(order1, car, 5),
                new OrderItem(order1, desk, 5),
                new OrderItem(order2, food, 5),
                new OrderItem(order2, car, 10),
                new OrderItem(order2, desk, 20),
                new OrderItem(order3, desk, 30),
                new OrderItem(order3, food, 30)
        );
        LocalDate statisticDate = LocalDate.now().minusDays(1);
        List<OrderStatistics> orderStatistics = OrderStatistics.calculateOrderStatistics(orderItemList, statisticDate);

        assertAll(
                "상품 별 판매 금액을 집계한다.",
                () -> assertEquals(3, orderStatistics.size()),
                () -> assertEquals(15, orderStatistics.stream()
                        .filter(o -> o.getItemId().equals(1L))
                        .findFirst().get().getOrderCount()),
                () -> assertEquals(55, orderStatistics.stream()
                        .filter(o -> o.getItemId().equals(2L))
                        .findFirst().get().getOrderCount()),
                () -> assertEquals(35, orderStatistics.stream()
                        .filter(o -> o.getItemId().equals(3L))
                        .findFirst().get().getOrderCount())
        );
    }
}
