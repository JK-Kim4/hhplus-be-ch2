package kr.hhplus.be.server.domain.payment;

import jakarta.persistence.NoResultException;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public PaymentService(
            PaymentRepository paymentRepository,
            UserRepository userRepository,
            OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public PaymentCreateCommand.Response save(PaymentCreateCommand command) {
        Order order = orderRepository.findById(command.getOrderId())
                .orElseThrow(NoResultException::new);

        Payment payment = paymentRepository.save(new Payment(order));

        order.registerPayment(payment);

        return new PaymentCreateCommand.Response(payment);
    }


    @Transactional
    public void processPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId);

        payment.pay();





    }

    public Payment findById(Long paymentId) {
        return paymentRepository.findById(paymentId);
    }
}
