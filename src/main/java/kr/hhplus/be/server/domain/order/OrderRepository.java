package kr.hhplus.be.server.domain.order;

import java.util.List;

public interface OrderRepository {

    void flush();

    Order save(Order order);

    List<Order> findByUserId(Long userId);
}
