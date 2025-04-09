package kr.hhplus.be.server.domain.order;

import jakarta.persistence.NoResultException;
import kr.hhplus.be.server.domain.item.ItemRepository;
import kr.hhplus.be.server.domain.order.command.OrderCreateCommand;
import kr.hhplus.be.server.domain.order.command.OrderItemCreateCommand;
import kr.hhplus.be.server.domain.order.orderItem.OrderItem;
import kr.hhplus.be.server.domain.order.orderItem.OrderItemRepository;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public OrderService(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            UserRepository userRepository,
            ItemRepository itemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Transactional
    public OrderCreateCommand.Response save(OrderCreateCommand command){
        User orderUser = userRepository.findById(command.getUserId())
                .orElseThrow(NoResultException::new);

        Order order = orderRepository.save(new Order(orderUser));

        List<OrderItem> orderItems = this.saveOrderItems(order, command.getOrderItems());
        order.calculateTotalPrice(orderItems);

        return new OrderCreateCommand.Response(order);
    }

    public List<OrderItem> saveOrderItems(Order order, List<OrderItemCreateCommand> command){
        List<OrderItem> orderItems = this.createOrderItems(order, command);
        orderItemRepository.saveList(orderItems);
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
