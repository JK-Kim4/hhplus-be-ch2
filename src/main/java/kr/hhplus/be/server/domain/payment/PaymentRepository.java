package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.domain.payment.paymentHistory.PaymentHistory;

import java.util.Optional;

public interface PaymentRepository {

    Payment save(Payment payment);

    Optional<Payment> findById(Long paymentId);

    PaymentHistory savePaymentHistory(PaymentHistory paymentHistory);
}
