package kr.hhplus.be.server.domain.order.orderItem;

import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderItems;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderItemsTest {


    @Test
    void calculateTotalPrice_shouldReturnSumOfPrices() {
        OrderItem item1 = OrderItem.of(1L, 1000, 2);  // 총 2000
        OrderItem item2 = OrderItem.of(2L, 1500, 1);  // 총 1500
        OrderItem item3 = OrderItem.of(3L, 500, 3);   // 총 1500

        List<OrderItem> items = List.of(item1, item2, item3);
        OrderItems orderItems = new OrderItems(items);
        
        int total = orderItems.calculateTotalPrice();

        assertEquals(5000, total); // price 합만 계산됨
    }


}
