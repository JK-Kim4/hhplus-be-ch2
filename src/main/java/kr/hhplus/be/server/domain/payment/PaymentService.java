package kr.hhplus.be.server.domain.payment;

import jakarta.persistence.NoResultException;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.payment.paymentHistory.PaymentHistory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public PaymentCreateCommand.Response save(PaymentCreateCommand command) {
        Order order = orderRepository.findById(command.getOrderId())
                .orElseThrow(NoResultException::new);
        Payment payment = paymentRepository.save(new Payment(order));

        order.registerPayment(payment);

        this.savePaymentHistory(payment);

        return new PaymentCreateCommand.Response(payment);
    }


    @Transactional
    public PaymentProcessCommand.Response processPayment(PaymentProcessCommand command) {
        Payment payment = paymentRepository.findById(command.getPaymentId())
                .orElseThrow(NoResultException::new);

        payment.pay();
        payment.getOrder().updateOrderStatus(OrderStatus.PAYMENT_COMPLETED);

        this.savePaymentHistory(payment);

        return new PaymentProcessCommand.Response(payment);
    }

    @Transactional(readOnly = true)
    public Payment findById(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(NoResultException::new);
    }

    private PaymentHistory savePaymentHistory(Payment payment) {
        return paymentRepository.savePaymentHistory(new PaymentHistory(payment));
    }
}
