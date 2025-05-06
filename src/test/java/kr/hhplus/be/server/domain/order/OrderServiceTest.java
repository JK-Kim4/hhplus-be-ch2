package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.DiscountPolicy;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

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

    @Test
    void 주문이_정상적으로_생성되었을경우_repository_save_1회_호출() {
        // given
        Long userId = 1L;
        Long productId = 10L;
        BigDecimal price = BigDecimal.valueOf(1000);
        int quantity = 2;

        OrderCommand.Create command = OrderCommand.Create.of(
                userId,
                null,  // 쿠폰 없음
                List.of(OrderCommand.Items.builder().productId(productId).price(price).quantity(quantity).build())
            );

        Product product = Product.create("테스트상품", price, 10);

        given(productRepository.findById(productId)).willReturn(Optional.of(product));
        given(orderRepository.findByUserId(userId)).willReturn(List.of());

        // when
        orderService.create(command);

        // then
        verify(orderRepository, times(1)).save(any(Order.class));  // save가 정확히 1번 호출됐는지 검증
    }

    @Test
    void create_상품가격불일치_예외발생() {
        // given
        Long userId = 1L;
        Long productId = 100L;
        BigDecimal actualPrice = BigDecimal.valueOf(1000);
        BigDecimal wrongPrice = BigDecimal.valueOf(900);
        int quantity = 1;

        OrderCommand.Create command = OrderCommand.Create.of(
                userId,
                null,  // 쿠폰 없음
                List.of(OrderCommand.Items.builder().productId(productId).price(wrongPrice).quantity(quantity).build())
        );

        Product product = Product.create("테스트상품", actualPrice, 10);
        given(productRepository.findById(productId)).willReturn(Optional.of(product));
        given(orderRepository.findByUserId(userId)).willReturn(List.of());

        // when & then
        assertThrows(IllegalArgumentException.class, () -> orderService.create(command));
    }

    @Test
    void create_재고부족_예외발생() {
        // given
        Long userId = 1L;
        Long productId = 100L;
        BigDecimal price = BigDecimal.valueOf(1000);
        int orderQuantity = 5;
        int stock = 2;

        OrderCommand.Create command = OrderCommand.Create.of(
                userId,
                null,  // 쿠폰 없음
                List.of(OrderCommand.Items.builder().productId(productId).price(price).quantity(orderQuantity).build())
        );

        Product product = Product.create("테스트상품", price, stock);
        given(productRepository.findById(productId)).willReturn(Optional.of(product));
        given(orderRepository.findByUserId(userId)).willReturn(List.of());

        // when & then
        assertThrows(IllegalStateException.class, () -> orderService.create(command));
    }

    @Test
    void create_쿠폰이_정상적으로_적용된다() {
        // given
        Long userId = 1L;
        Long productId = 100L;
        Long userCouponId = 200L;
        BigDecimal price = BigDecimal.valueOf(1000);


        OrderCommand.Create command = OrderCommand.Create.of(
                userId,
                userCouponId,
                List.of(OrderCommand.Items.builder().productId(productId).price(price).quantity(1).build())
        );

        Product product = Product.create("상품", price, 10);
        UserCoupon coupon = UserCoupon.issue(Coupon.create(
                "쿠폰 발급 동시성 테스트",
                5,
                DiscountPolicy.FLAT,
                BigDecimal.valueOf(5000),
                LocalDate.now().plusDays(3)
        ), userId);

        given(productRepository.findById(productId)).willReturn(Optional.of(product));
        given(orderRepository.findByUserId(userId)).willReturn(List.of());
        given(userCouponRepository.findById(userCouponId)).willReturn(Optional.of(coupon));

        // when
        orderService.create(command);

        // then
        verify(orderRepository).save(any(Order.class));
        verify(userCouponRepository).findById(userCouponId); // 쿠폰이 적용되었는지 확인
    }
}
