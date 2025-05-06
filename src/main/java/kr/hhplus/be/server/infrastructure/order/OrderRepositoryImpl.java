package kr.hhplus.be.server.infrastructure.order;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderItemJpaRepository orderItemJpaRepository;

    public OrderRepositoryImpl(OrderJpaRepository orderJpaRepository, OrderItemJpaRepository orderItemJpaRepository) {
        this.orderJpaRepository = orderJpaRepository;
        this.orderItemJpaRepository = orderItemJpaRepository;
    }

    @Override
    public void flush() {
        orderJpaRepository.flush();
    }

    @Override
    public Order save(Order order) {
        orderJpaRepository.save(order);
        orderItemJpaRepository.saveAll(order.getOrderItems());
        return order;
    }

    @Override
    public List<Order> findByUserId(Long userId) {
        return orderJpaRepository.findByUserId(userId);
    }

    @Override
    public Optional<Order> findById(Long orderId) {
        return orderJpaRepository.findById(orderId);
    }

    @Override
    public List<OrderItem> findOrderItemsByOrderId(Long orderId) {
        return orderItemJpaRepository.findByOrderId(orderId);
    }
}
