package kr.hhplus.be.server.domain.order;

import jakarta.persistence.NoResultException;
import kr.hhplus.be.server.domain.item.ItemRepository;
import kr.hhplus.be.server.domain.order.command.OrderCreateCommand;
import kr.hhplus.be.server.domain.order.command.OrderItemCreateCommand;
import kr.hhplus.be.server.domain.order.orderItem.OrderItem;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public OrderService(
            OrderRepository orderRepository,
            UserRepository userRepository,
            ItemRepository itemRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Transactional
    public OrderCreateCommand.Response createOrder(OrderCreateCommand command){
        User orderUser = userRepository.findById(command.getUserId())
                .orElseThrow(NoResultException::new);

        Order order = new Order(orderUser);

        List<OrderItem> orderItems = this.saveOrderItems(order, command.getOrderItems()).getOrderItems();

        order.calculateTotalPrice(orderItems);
        orderItems.forEach(OrderItem::decreaseItemStock);

        orderRepository.save(order);

        return new OrderCreateCommand.Response(order);
    }

    @Transactional
    public OrderItemCreateCommand.Response saveOrderItems(Order order, List<OrderItemCreateCommand> command){
        List<OrderItem> orderItems = this.createOrderItems(order, command);
        orderRepository.saveOrderItemList(orderItems);
        return new OrderItemCreateCommand.Response(orderItems);
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

    private List<OrderItem> createOrderItems(Order order, List<OrderItemCreateCommand> command){

        return command.stream().map(
                (commandItem) ->
                        commandItem.toEntity(
                                order,
                                itemRepository.findById(commandItem.getItemId())
                                    .orElseThrow(NoResultException::new),
                                commandItem.getQuantity())).toList();
    }

}
