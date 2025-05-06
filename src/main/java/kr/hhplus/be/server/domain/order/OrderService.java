package kr.hhplus.be.server.domain.order;

import jakarta.persistence.NoResultException;
import kr.hhplus.be.server.domain.coupon.UserCouponRepository;
import kr.hhplus.be.server.domain.product.Price;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.domain.user.Orders;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public OrderInfo.Create create(OrderCommand.Create command){
        //1.사용자 미결제 주문 여부 검증
        List<Order> userOrders = orderRepository.findByUserId(command.getUserId());
        Orders.validateNoPendingOrders(userOrders);

        //2.상품 주문 가능 여부 검증
        List<OrderItem> orderItems = command.items.stream()
                .map(this::toOrderItem)
                .toList();

        //3.주문 생성
        Order order = Order.create(command.getUserId(), orderItems);

        //4.할인 쿠폰 적용
        Optional.ofNullable(command.getUserCouponId())
                .map(userCouponId -> userCouponRepository.findById(command.getUserCouponId())
                        .orElseThrow(NoResultException::new))
                .ifPresent(order::applyCoupon);

        //5.주문 저정
        orderRepository.save(order);

        return OrderInfo.Create.from(order);
    }

    private OrderItem toOrderItem(OrderCommand.Items item) {
        Product product = productRepository.findById(item.getProductId())
                .orElseThrow(NoResultException::new);
        product.validateOrder(item.getPrice(), item.getQuantity());
        return OrderItem.create(product.getId(), Price.of(item.getPrice()), item.getQuantity());
    }

}
