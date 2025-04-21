package kr.hhplus.be.server.domain.payment;

import jakarta.persistence.NoResultException;
import kr.hhplus.be.server.domain.payment.paymentHistory.PaymentHistory;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }


    public void save(Payment payment) {
        paymentRepository.save(payment);
    }

    public Payment findById(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(NoResultException::new);
    }

    private PaymentHistory savePaymentHistory(Payment payment) {
        return paymentRepository.savePaymentHistory(new PaymentHistory(payment));
    }
}
