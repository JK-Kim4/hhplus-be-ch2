package kr.hhplus.be.server.infrastructure.order;

import io.lettuce.core.dynamic.annotation.Param;
import kr.hhplus.be.server.domain.order.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface OrderItemJpaRepository extends JpaRepository<OrderItem, Long> {

    @Query("select oi from OrderItem oi where oi.order.id = :orderId")
    List<OrderItem> findByOrderId(Long orderId);


    @Query("select oi from OrderItem oi where oi.order.id in :orderIds")
    List<OrderItem> findOrderItemsByOrderIds(@Param("orderIds") Set<Long> orderIds);
}
