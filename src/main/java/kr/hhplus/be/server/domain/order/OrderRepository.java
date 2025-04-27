package kr.hhplus.be.server.domain.order;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    Order save(Order order);

    Optional<Order> findById(Long id);

    List<Order> findAllByOrderDate(LocalDate date);

    List<Order> findByDateAndStatus(LocalDate orderedDate, OrderStatus status);

    List<OrderItem> findOrderItemsByOrderIds(List<Long> orderIds);

    List<OrderItem> findOrderItemsByOrderId(Long orderId);

    OrderItem saveOrderItem(OrderItem orderItem);

    List<OrderItem> saveOrderItemList(List<OrderItem> orderItemList);
}
