package kr.hhplus.be.server.domain.order;

import jakarta.persistence.NoResultException;
import kr.hhplus.be.server.domain.item.ItemRepository;
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
    private final ItemRepository itemRepository;

    public OrderService(OrderRepository orderRepository, ItemRepository itemRepository) {
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
    }

    public OrderInfo.Create createOrder(OrderCommand.Create command){
        List<OrderItem> orderItemV2List = command.getOrderItems().stream()
                .map(OrderCommand.OrderItemCreate::toEntity)
                .toList();

        OrderItems orderItems = new OrderItems(orderItemV2List);
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

    public List<OrderItem> createOrderItems(Order order, List<OrderCommand.OrderItemCreate> orderItemCommands) {

        if(orderItemCommands.isEmpty()){
            throw new IllegalArgumentException("주문 상품이 존재하지않습니다.");
        }

        return orderItemCommands.stream()
                .map(command -> new OrderItem(
                        order,
                        itemRepository.findById(command.getItemId())
                                .orElseThrow(NoResultException::new),
                        command.getPrice(),
                        command.getQuantity()))
                .toList();
    }

    public void save(Order order) {
        orderRepository.save(order);
    }

    public Order findById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(NoResultException::new);
    }
}
