package kr.hhplus.be.server.domain.payment;

import java.util.Optional;

public interface PaymentRepository {

    void flush();

    Payment save(Payment payment);

    Optional<Payment> findById(Long paymentId);
}
