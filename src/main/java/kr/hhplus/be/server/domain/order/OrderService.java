package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.order.command.OrderCommand;
import kr.hhplus.be.server.domain.order.command.OrderInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderInfo.Create createOrder(OrderCommand.Create command){
        List<OrderItem> orderItemV2List = command.getOrderItems().stream()
                .map(OrderCommand.OrderItem::toEntity)
                .toList();

        OrderItems orderItems = new OrderItems(orderItemV2List);
//        OrderItems orderItems = OrderItems.from(command.getOrderItems());
        Order order = orderRepository.save(command.toEntity(orderItemV2List));
        order.registerOrderItems(orderItems);

        return OrderInfo.Create.from(order);
    }


    @Transactional(readOnly = true)
    public List<Order> findOrdersByDateAndStatus(LocalDate orderedDate, OrderStatus status) {
        List<Order> orders = orderRepository.findByDateAndStatus(orderedDate, status);

        return orders;
    }

    @Transactional(readOnly = true)
    public List<OrderItem> findOrderItemsByOrderIds(List<Order> orders) {
        List<Long> orderIds = orders.stream().map(Order::getId).collect(Collectors.toList());
        List<OrderItem> orderItems = orderRepository.findOrderItemsByOrderIds(orderIds);

        return orderItems;
    }
}
