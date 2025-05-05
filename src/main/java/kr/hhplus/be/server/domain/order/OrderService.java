package kr.hhplus.be.server.domain.order;

import jakarta.persistence.NoResultException;
import kr.hhplus.be.server.domain.coupon.userCoupon.UserCoupon;
import kr.hhplus.be.server.domain.coupon.userCoupon.UserCouponRepository;
import kr.hhplus.be.server.domain.item.Item;
import kr.hhplus.be.server.domain.item.ItemRepository;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentRepository;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final UserCouponRepository userCouponRepository;
    private final PaymentRepository paymentRepository;

    public OrderService(
            OrderRepository orderRepository,
            UserRepository userRepository,
            ItemRepository itemRepository,
            UserCouponRepository userCouponRepository,
            PaymentRepository paymentRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.userCouponRepository = userCouponRepository;
        this.paymentRepository = paymentRepository;
    }

    public void save(Order order) {
        orderRepository.save(order);
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

    public OrderInfo.Create createV2(OrderCommand.CreateV2 command) {
        User user = userRepository.findById(command.getUserId())
                .orElseThrow(NoResultException::new);

        Order order = Order.createWithUser(user);

        command.getOrderItems().stream()
                .map(item -> {
                    Item temp = itemRepository.findById(item.getItemId())
                            .orElseThrow(NoResultException::new);
                    return OrderItem.createWithItemAndPriceAndQuantity(temp, item.getPrice(), item.getQuantity());
                })
                .forEach(order::addOrderItem);

        order.calculateTotalPrice();

        Optional.ofNullable(command.getUserCouponId())
                .ifPresent(userCouponId -> {
                    UserCoupon userCoupon = userCouponRepository.findById(userCouponId)
                            .orElseThrow(NoResultException::new);
                    order.applyDiscount(userCoupon);
                });

        orderRepository.save(order);

        return OrderInfo.Create.from(order);
    }

    public OrderInfo.Payment getOrderForPayment(Long orderId) {
        //주문 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(NoResultException::new);

        //결과 반환
        return OrderInfo.Payment.from(order);
    }

    public void registerPayment(OrderCommand.RegisterPayment command) {
        Order order = orderRepository.findById(command.getOrderId()).orElseThrow(NoResultException::new);
        Payment payment = paymentRepository.findById(command.getPaymentId()).orElseThrow(NoResultException::new);

        order.registerPayment(payment);
        orderRepository.save(order);
    }
}
