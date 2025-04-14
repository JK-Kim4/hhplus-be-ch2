package kr.hhplus.be.server.domain.payment;

import jakarta.persistence.NoResultException;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.payment.command.PaymentCreateCommand;
import kr.hhplus.be.server.domain.payment.command.PaymentInfo;
import kr.hhplus.be.server.domain.payment.command.PaymentProcessCommand;
import kr.hhplus.be.server.domain.payment.paymentHistory.PaymentHistory;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public PaymentService(
            PaymentRepository paymentRepository,
            OrderRepository orderRepository,
            UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public PaymentInfo.Create createPayment(PaymentCreateCommand command) {
        Order order = orderRepository.findById(command.getOrderId())
                .orElseThrow(NoResultException::new);

        User user = userRepository.findById(order.getUserId())
                .orElseThrow(NoResultException::new);

        Payment payment = paymentRepository.save(new Payment(order, user));
        order.registerPayment(payment);

        this.savePaymentHistory(payment);

        return new PaymentInfo.Create(payment.getId());
    }


    @Transactional
    public PaymentInfo.Process processPayment(PaymentProcessCommand command) {
        Payment payment = paymentRepository.findById(command.getPaymentId())
                .orElseThrow(NoResultException::new);

        payment.pay();
        payment.getOrder().updateOrderStatus(OrderStatus.PAYMENT_COMPLETED);

        this.savePaymentHistory(payment);

        return new PaymentInfo.Process(payment);
    }

    @Transactional(readOnly = true)
    public Payment findById(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(NoResultException::new);
    }

    private PaymentHistory savePaymentHistory(Payment payment) {
        return paymentRepository.savePaymentHistory(new PaymentHistory(payment));
    }

    public void save(Payment payment) {
        paymentRepository.save(payment);
    }
}
