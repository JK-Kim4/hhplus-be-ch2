package kr.hhplus.be.server.domain.payment;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository {

    void flush();

    Payment save(Payment payment);

    Optional<Payment> findById(Long paymentId);

    List<Payment> findAllByPaidDate(LocalDate targetDate);

    Optional<Payment> findByOrderId(Long orderId);
}
