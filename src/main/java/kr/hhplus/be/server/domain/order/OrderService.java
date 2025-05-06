package kr.hhplus.be.server.domain.order;

import jakarta.persistence.NoResultException;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.coupon.UserCouponRepository;
import kr.hhplus.be.server.domain.product.Price;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserCouponRepository userCouponRepository;


    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, UserCouponRepository userCouponRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userCouponRepository = userCouponRepository;
    }

    public void validationUserOrder(Long userId){
        List<Order> userOrders = orderRepository.findByUserId(userId);
        Orders.validateNoPendingOrders(userOrders);
    }

    public void validationOrderItems(List<OrderCommand.Items> command){
        List<OrderItem> orderItems = command.stream()
                .map(this::toOrderItem)
                .toList();
    }

    public OrderInfo.Create createOrder(OrderCommand.Create command){

        Order order = Order.create(
                command.getUserId(),
                command.getItems().stream()
                        .map(this::toOrderItem)
                        .toList());
        orderRepository.save(order);

        return OrderInfo.Create.from(order);
    }

    public OrderInfo.ApplyCoupon applyCoupon(OrderCommand.ApplyCoupon command){
        UserCoupon userCoupon = userCouponRepository.findById(command.getUserCouponId())
                .orElseThrow(NoResultException::new);
        Order order = orderRepository.findById(command.getOrderId())
                .orElseThrow(NoResultException::new);

        order.applyCoupon(userCoupon);

        return OrderInfo.ApplyCoupon.from(order);
    }

    private OrderItem toOrderItem(OrderCommand.Items item) {
        Product product = productRepository.findById(item.getProductId())
                .orElseThrow(NoResultException::new);
        product.validateOrder(item.getPrice(), item.getQuantity());
        return OrderItem.create(product.getId(), Price.of(item.getPrice()), item.getQuantity());
    }
}
