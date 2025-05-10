package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.product.Price;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderItemsTest {

    @Test
    void 주문_상품_목록의_총_주문가격을_계산한다() {
        OrderItem item1 = OrderItem.create(1L, Price.of(BigDecimal.valueOf(1000)), 2); // 2000
        OrderItem item2 = OrderItem.create(1L, Price.of(BigDecimal.valueOf(1500)), 1); // 1500

        OrderItems items = new OrderItems(List.of(item1, item2));

        Price total = items.calculateTotalAmount();

        assertEquals(BigDecimal.valueOf(3500), total.getAmount());
    }
}
