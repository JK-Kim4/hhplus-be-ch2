package kr.hhplus.be.server.domain.order.orderItem;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository {

    OrderItem save(OrderItem orderItem);

    List<OrderItem> saveList(List<OrderItem> orderItemList);
}
