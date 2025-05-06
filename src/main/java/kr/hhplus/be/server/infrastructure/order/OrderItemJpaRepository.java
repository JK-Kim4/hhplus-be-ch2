package kr.hhplus.be.server.infrastructure.order;

import kr.hhplus.be.server.domain.order.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderItemJpaRepository extends JpaRepository<OrderItem, Long> {

    @Query("select oi from OrderItem oi where oi.order.id = :orderId")
    List<OrderItem> findByOrderId(Long orderId);
}
