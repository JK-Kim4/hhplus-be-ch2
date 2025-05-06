package kr.hhplus.be.server.domain.order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    void flush();

    Order save(Order order);

    List<Order> findByUserId(Long userId);

    Optional<Order> findById(Long orderId);
}
