package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.product.Price;
import kr.hhplus.be.server.domain.product.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderItemTest {

    @Test
    void 상품의_가격과_수량으로_결제금액을_계산한다() {
        Product product = Product.create("product", BigDecimal.valueOf(10_000), 10);
        OrderItem item = OrderItem.create(1L, Price.of(product.getAmount()), 5);

        Price amount = item.calculateAmount();

        assertEquals(BigDecimal.valueOf(10_000 * 5), amount.getAmount());
    }

    @Test
    void 주문상품에_주문정보를_할당한다() {
        Order order = new Order();
        OrderItem item = new OrderItem();
        item.assignToOrder(order);

        assertEquals(order, item.getOrder());
    }
}
