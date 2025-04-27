package kr.hhplus.be.server.domain.order;

import jakarta.persistence.NoResultException;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.user.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
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

    public Order findById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(NoResultException::new);
    }

    public List<Order> findOrdersByDateAndStatus(LocalDate orderedDate, OrderStatus status) {
        return orderRepository.findByDateAndStatus(orderedDate, status);
    }

    public List<Order> findAllByOrderDate(LocalDate orderedDate) {
        return orderRepository.findAllByOrderDate(orderedDate);
    }

    public List<OrderItem> findAllByOrderIds(List<Long> orderIds) {
        return orderRepository.findOrderItemsByOrderIds(orderIds);
    }

    public Order create(User user, List<OrderItem> orderItems) {
        user.canCreateOrder();
        Order order = Order.createWithItems(user, orderItems);
        return orderRepository.save(order);
    }
}
