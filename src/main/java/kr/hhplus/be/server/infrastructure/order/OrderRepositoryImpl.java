package kr.hhplus.be.server.infrastructure.order;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.order.OrderStatus;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderItemJpaRepository orderItemJpaRepository;

    public OrderRepositoryImpl(
            OrderJpaRepository orderJpaRepository,
            OrderItemJpaRepository orderItemJpaRepository) {
        this.orderJpaRepository = orderJpaRepository;
        this.orderItemJpaRepository = orderItemJpaRepository;
    }

    @Override
    public Order save(Order order) {
        return orderJpaRepository.save(order);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return orderJpaRepository.findById(id);
    }

    @Override
    public List<Order> findAllByOrderDate(LocalDate date) {
        return orderJpaRepository.findByOrderDate(date);
    }

    @Override
    public List<Order> findByDateAndStatus(LocalDate orderedDate, OrderStatus status) {
        return orderJpaRepository.findByDateAndStatus(orderedDate, status);
    }

    @Override
    public List<OrderItem> findOrderItemsByOrderIds(List<Long> orderIds) {
        return orderItemJpaRepository.findByOrderIdIn(orderIds);
    }

    @Override
    public List<OrderItem> findOrderItemsByOrderId(Long orderId) {
        return orderItemJpaRepository.findByOrderId(orderId);
    }

    @Override
    public OrderItem saveOrderItem(OrderItem orderItem) {
        return orderItemJpaRepository.save(orderItem);
    }

    @Override
    public List<OrderItem> saveOrderItemList(List<OrderItem> orderItemList) {
        return orderItemJpaRepository.saveAll(orderItemList);
    }

}
