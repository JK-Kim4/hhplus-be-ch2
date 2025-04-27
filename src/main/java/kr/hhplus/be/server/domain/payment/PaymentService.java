package kr.hhplus.be.server.domain.payment;

import jakarta.persistence.NoResultException;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.user.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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

    public Payment create(Order order, User user) {
        Payment payment = Payment.createWithOrderValidation(order, user);
        return paymentRepository.save(payment);
    }

    public void processPayment(Payment payment, Order order) {
        payment.isPayable();
        payment.pay();
        order.deductOrderItemStock();
        order.updateOrderStatus(OrderStatus.PAYMENT_COMPLETED);
        order.updateOrderDate(LocalDate.now());
    }

    public void success(Payment payment, Order order) {
        payment.updatePaymentResult();
        order.updateOrderStatus(OrderStatus.PAYMENT_COMPLETED);
    }
}
