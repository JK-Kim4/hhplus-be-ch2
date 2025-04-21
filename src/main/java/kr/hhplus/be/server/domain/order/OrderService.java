package kr.hhplus.be.server.domain.order;

import jakarta.persistence.NoResultException;
import kr.hhplus.be.server.domain.payment.Payment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void save(Order order) {
        orderRepository.save(order);
    }

    public void processPayment(Order order, Payment payment) {
        payment.isPayable();
        payment.pay();
        order.deductOrderItemStock();
        order.updateOrderStatus(OrderStatus.PAYMENT_COMPLETED);
    }

    @Transactional(readOnly = true)
    public Order findById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(NoResultException::new);
    }

    @Transactional(readOnly = true)
    public List<Order> findOrdersByDateAndStatus(LocalDate orderedDate, OrderStatus status) {
        return orderRepository.findByDateAndStatus(orderedDate, status);
    }

    @Transactional(readOnly = true)
    public List<OrderItem> findOrderItemsByOrderIds(List<Order> orders) {
        List<Long> orderIds = orders.stream()
                .map(Order::getId)
                .collect(Collectors.toList());
        return orderRepository.findOrderItemsByOrderIds(orderIds);
    }
}
