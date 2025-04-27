package kr.hhplus.be.server.infrastructure.order;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {

    @Query(value = "SELECT * FROM Orders WHERE DATE(created_at) = :orderedDate and order_status = :status", nativeQuery = true)
    List<Order> findByDateAndStatus(@Param("orderedDate")LocalDate orderedDate,
                                    @Param("status")OrderStatus status);


    List<Order> findByOrderDate(LocalDate date);
}
