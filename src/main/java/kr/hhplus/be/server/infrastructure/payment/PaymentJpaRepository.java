package kr.hhplus.be.server.infrastructure.payment;

import io.lettuce.core.dynamic.annotation.Param;
import kr.hhplus.be.server.domain.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PaymentJpaRepository extends JpaRepository<Payment, Long> {

    @Query("select p from Payment p where p.paidAt = :targetDate")
    List<Payment> findAllByPaidAt(@Param("targetDate") LocalDate targetDate);

    @Query("select p from Payment p where p.paidAt between :start and :end")
    List<Payment> findAllByPaidAtBetween(@Param("start") LocalDateTime start, @Param("end")LocalDateTime end);

    Optional<Payment> findByOrderId(Long orderId);
}
