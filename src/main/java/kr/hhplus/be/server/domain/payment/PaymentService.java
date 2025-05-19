package kr.hhplus.be.server.domain.payment;

import jakarta.persistence.NoResultException;
import kr.hhplus.be.server.domain.balance.Balance;
import kr.hhplus.be.server.domain.balance.BalanceRepository;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final BalanceRepository balanceRepository;


    public PaymentService(
            PaymentRepository paymentRepository,
            OrderRepository orderRepository,
            BalanceRepository balanceRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.balanceRepository = balanceRepository;
    }

    public PaymentInfo.Create create(PaymentCommand.Create command){
        Order order = orderRepository.findById(command.getOrderId())
                .orElseThrow(NoResultException::new);
        Payment payment = Payment.create(order);

        paymentRepository.save(payment);
        return PaymentInfo.Create.from(payment);
    }

    public PaymentInfo.Pay pay(PaymentCommand.Pay command){
        Payment payment = paymentRepository.findById(command.getPaymentId())
                .orElseThrow(NoResultException::new);
        Balance balance = balanceRepository.findByUserId(command.getUserId())
                .orElseThrow(NoResultException::new);

        payment.pay(balance);
        paymentRepository.save(payment);

        return PaymentInfo.Pay.from(payment);
    }

    public PaymentInfo.Complete complete(PaymentCommand.Complete command){
        Payment payment = paymentRepository.findById(command.getPaymentId())
                .orElseThrow(NoResultException::new);
        Order order = orderRepository.findById(command.getOrderId())
                .orElseThrow(NoResultException::new);

        payment.complete(order);
        paymentRepository.save(payment);

        return PaymentInfo.Complete.from(payment);
    }

    public PaymentInfo.Payments findAllByPaidDate(LocalDate targetDate) {




        return null;
    }
}
