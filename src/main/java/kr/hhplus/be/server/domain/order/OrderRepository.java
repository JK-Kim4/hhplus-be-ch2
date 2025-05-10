package kr.hhplus.be.server.domain.order;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface OrderRepository {

    void flush();

    Order save(Order order);

    List<Order> findByUserId(Long userId);

    Optional<Order> findById(Long orderId);

    List<OrderItem> findOrderItemsByOrderId(Long orderId);

    List<OrderItem> findOrderItemsByOrderIds(Set<Long> targetOrderIds);
}
