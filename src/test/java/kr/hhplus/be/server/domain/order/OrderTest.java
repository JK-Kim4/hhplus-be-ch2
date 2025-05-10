package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.DiscountPolicy;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.product.Price;
import kr.hhplus.be.server.domain.user.User;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {

    @Test
    void 주문_상품이_비어있으면_주문_생성_오류(){
        assertThrows(IllegalArgumentException.class, () -> Order.create(1L, null));
        assertThrows(IllegalArgumentException.class, () -> Order.create(1L, List.of()));
    }

    @Test
    void 주문_생성시_총계산금액과_주문상태가_갱신된다(){
        OrderItem item1 = OrderItem.create(1L, Price.of(BigDecimal.valueOf(10_000)), 10); // 100_000
        OrderItem item2 = OrderItem.create(2L, Price.of(BigDecimal.valueOf(35_000)), 10); // 350_000

        List<OrderItem> items = List.of(item1, item2);

        Order order = Order.create(1L, items);

        assertAll(
                () -> assertEquals(BigDecimal.valueOf(450_000), order.getTotalAmount()),
                () -> assertEquals(BigDecimal.valueOf(450_000), order.getFinalAmount()),
                () -> assertEquals(OrderStatus.ORDER_CREATED, order.getStatus())
        );
    }

    @Test
    void 정률_할인_쿠폰_정보를_전달받아_최종_결제_금액을_계산한다(){
        //given
        User user = User.builder()
                .id(1L)
                .name("tester").build();

        Coupon coupon = Coupon.create(
                "10% 할인 쿠폰",
                100,
                DiscountPolicy.RATE,
                BigDecimal.valueOf(10),
                LocalDate.now().plusDays(7)
        );

        UserCoupon userCoupon = UserCoupon.issue(coupon, user.getId());

        OrderItem item1 = OrderItem.create(1L, Price.of(BigDecimal.valueOf(10_000)), 10); // 100_000
        OrderItem item2 = OrderItem.create(2L, Price.of(BigDecimal.valueOf(35_000)), 10); // 350_000

        List<OrderItem> items = List.of(item1, item2);

        Order order = Order.create(1L, items);

        //when
        order.applyCoupon(userCoupon);

        //then
        assertAll(
                () -> assertEquals(BigDecimal.valueOf(450_000), order.getTotalAmount()),
                () -> assertEquals(BigDecimal.valueOf(405000), order.getFinalAmount()),    // 총 결제금액 450,000원의 10% 할인쿠폰 적용
                () -> assertEquals(OrderStatus.ORDER_CREATED, order.getStatus())
        );
    }

    @Test
    void 정액_할인_쿠폰_정보를_전달받아_최종_결제_금액을_계산한다(){
        //given
        User user = User.builder()
                .id(1L)
                .name("tester").build();

        Coupon coupon = Coupon.create(
                "10% 할인 쿠폰",
                100,
                DiscountPolicy.FLAT,
                BigDecimal.valueOf(10_000),
                LocalDate.now().plusDays(7)
        );

        UserCoupon userCoupon = UserCoupon.issue(coupon, user.getId());

        OrderItem item1 = OrderItem.create(1L, Price.of(BigDecimal.valueOf(10_000)), 10); // 100_000
        OrderItem item2 = OrderItem.create(2L, Price.of(BigDecimal.valueOf(35_000)), 10); // 350_000

        List<OrderItem> items = List.of(item1, item2);

        Order order = Order.create(1L, items);

        //when
        order.applyCoupon(userCoupon);

        //then
        assertAll(
                () -> assertEquals(BigDecimal.valueOf(450_000), order.getTotalAmount()),
                () -> assertEquals(BigDecimal.valueOf(440_000), order.getFinalAmount()),    // 총 결제금액 450,000원의 10,000원 할인쿠폰 적용
                () -> assertEquals(OrderStatus.ORDER_CREATED, order.getStatus())
        );
    }

    @Test
    void 할인금액이_주문금액보다_클경우_최종_결제금액은_0원(){
        //given
        User user = User.builder()
                .id(1L)
                .name("tester").build();

        Coupon coupon = Coupon.create(
                "10% 할인 쿠폰",
                100,
                DiscountPolicy.FLAT,
                BigDecimal.valueOf(150_000),
                LocalDate.now().plusDays(7)
        );

        UserCoupon userCoupon = UserCoupon.issue(coupon, user.getId());

        OrderItem item1 = OrderItem.create(1L, Price.of(BigDecimal.valueOf(10_000)), 10); // 100_000

        List<OrderItem> items = List.of(item1);

        Order order = Order.create(1L, items);

        //when
        order.applyCoupon(userCoupon);

        //then
        assertEquals(BigDecimal.ZERO, order.getFinalAmount());
    }
}
