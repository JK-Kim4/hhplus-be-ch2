package kr.hhplus.be.server.domain.order;

import jakarta.persistence.NoResultException;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.coupon.UserCouponRepository;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserCouponRepository userCouponRepository;

    @InjectMocks
    private OrderService orderService;

    // 1. validationUserOrder()
    @Test
    void 사용자_미결제_주문_여부를_검증한다() {
        given(orderRepository.findByUserId(1L)).willReturn(List.of());

        assertDoesNotThrow(() -> orderService.validationUserOrder(1L));
    }

    @Test
    void 미결제_주문이_존재할경우_오류반환() {
        Order unpaidOrder = mock(Order.class);
        given(unpaidOrder.getStatus()).willReturn(OrderStatus.ORDER_CREATED);
        given(orderRepository.findByUserId(1L)).willReturn(List.of(unpaidOrder));

        assertThrows(IllegalStateException.class, () -> orderService.validationUserOrder(1L));
    }

    // 2. applyCoupon()

    @Test
    void 쿠폰_적용() {
        Long orderId = 1L;
        Long couponId = 2L;

        Order order = mock(Order.class);
        UserCoupon coupon = mock(UserCoupon.class);

        given(orderRepository.findById(orderId)).willReturn(Optional.of(order));
        given(userCouponRepository.findById(couponId)).willReturn(Optional.of(coupon));

        OrderCommand.ApplyCoupon command = OrderCommand.ApplyCoupon.of(orderId, couponId);

        OrderInfo.ApplyCoupon result = orderService.applyCoupon(command);

        verify(order).applyCoupon(coupon);
        assertThat(result).isNotNull();
    }

    @Test
    void 쿠폰이_존재하지않을경우_쿠폰적용시_예외() {
        Long orderId = 1L;
        Long couponId = 2L;

        given(userCouponRepository.findById(couponId)).willReturn(Optional.empty());

        OrderCommand.ApplyCoupon command = OrderCommand.ApplyCoupon.of(orderId, couponId);

        assertThrows(NoResultException.class, () -> orderService.applyCoupon(command));
    }

    @Test
    void 주문이_존재하지않을경우_쿠폰적용시_예외() {
        Long orderId = 1L;
        Long couponId = 2L;

        UserCoupon coupon = mock(UserCoupon.class);

        given(userCouponRepository.findById(couponId)).willReturn(Optional.of(coupon));
        given(orderRepository.findById(orderId)).willReturn(Optional.empty());

        OrderCommand.ApplyCoupon command = OrderCommand.ApplyCoupon.of(orderId, couponId);

        assertThrows(NoResultException.class, () -> orderService.applyCoupon(command));
    }

    // 3. createOrder()

    @Test
    void 주문정보를_생성하고_저장한다() {
        Long userId = 1L;
        BigDecimal price = BigDecimal.valueOf(1000);
        Long productId = 10L;

        List<OrderCommand.Items> orderItems = List.of(
                OrderCommand.Items.of(productId, price, 2)
        );
        OrderCommand.Create command = OrderCommand.Create.of(userId, null, orderItems);

        given(productRepository.findById(productId)).willReturn(Optional.ofNullable(Product.create("test", price, 10)));

        OrderInfo.Create result = orderService.createOrder(command);

        assertThat(result).isNotNull();
        verify(orderRepository).save(any(Order.class));
    }
}
