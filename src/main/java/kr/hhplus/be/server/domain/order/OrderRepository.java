package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.order.orderItem.OrderItem;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findById(Long id);

    List<OrderItem> findOrderItemsByOrderId(Long orderId);

    OrderItem saveOrderItem(OrderItem orderItem);

    List<OrderItem> saveOrderItemList(List<OrderItem> orderItemList);


}
