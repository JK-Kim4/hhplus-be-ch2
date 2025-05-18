package kr.hhplus.be.server.support.domainSupport;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.product.Price;

import java.math.BigDecimal;
import java.util.List;

public class OrderDomainSupporter {

    public static Order 기본_주문_생성(){
        List<OrderItem> items = List.of(
            OrderItem.create(1L, Price.of(BigDecimal.valueOf(10_000)), 10),
            OrderItem.create(2L, Price.of(BigDecimal.valueOf(10_000)), 10),
            OrderItem.create(3L, Price.of(BigDecimal.valueOf(10_000)), 10)
        );

        return Order.create(1L, items);
    }

    public static Order 사용자_고유번호를_전달받아_기본_주문_생성(Long userId){
        List<OrderItem> items = List.of(
                OrderItem.create(1L, Price.of(BigDecimal.valueOf(10_000)), 10),
                OrderItem.create(2L, Price.of(BigDecimal.valueOf(10_000)), 10),
                OrderItem.create(3L, Price.of(BigDecimal.valueOf(10_000)), 10)
        );

        return Order.create(userId, items);
    }
}
