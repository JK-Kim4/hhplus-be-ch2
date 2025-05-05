package kr.hhplus.be.server.domain.payment;

import jakarta.persistence.NoResultException;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.user.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentService(
            PaymentRepository paymentRepository,
            OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
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

    public void success(Payment payment, Order order) {
        payment.updatePaymentResponseDateTime(LocalDateTime.now());
        order.updateOrderStatus(OrderStatus.PAYMENT_COMPLETED);
    }

    public PaymentInfo.Create create(PaymentCommand.Create command) {
        Order order = orderRepository.findById(command.getOrderId())
                .orElseThrow(NoResultException::new);
        Payment payment = Payment.createWithOrder(order);
        paymentRepository.save(payment);
        return PaymentInfo.Create.from(payment);
    }
}
