package kr.hhplus.be.server.domain.order;

import jakarta.persistence.NoResultException;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.user.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<OrderItem> findOrderItemsByOrderIds(List<Order> orders) {
        List<Long> orderIds = orders.stream()
                .map(Order::getId)
                .collect(Collectors.toList());
        return orderRepository.findOrderItemsByOrderIds(orderIds);
    }

    public Order create(User user, List<OrderItem> orderItems) {
        user.canCreateOrder();
        Order order = Order.createWithItems(user, orderItems);
        return orderRepository.save(order);
    }
}
