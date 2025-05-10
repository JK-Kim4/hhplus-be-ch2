package kr.hhplus.be.server.domain.coupon;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CouponTest {

    @Test
    void 쿠폰을_정상적으로_생성할_수_있다() {
        Coupon coupon = Coupon.create(
                "10% 할인 쿠폰",
                100,
                DiscountPolicy.RATE,
                BigDecimal.valueOf(10),
                LocalDate.now().plusDays(7)
        );

        assertThat(coupon.getName()).isEqualTo("10% 할인 쿠폰");
        assertThat(coupon.getDiscountPolicy()).isEqualTo(DiscountPolicy.RATE);
        assertThat(coupon.getDiscountAmount()).isEqualByComparingTo("10");
    }

    @Test
    void 쿠폰_수량이_감소한다() {
        Coupon coupon = Coupon.create(
                "정액 할인 쿠폰",
                3,
                DiscountPolicy.FLAT,
                BigDecimal.valueOf(5000),
                LocalDate.now().plusDays(3)
        );

        coupon.decreaseQuantity();
        assertThat(coupon.getQuantity()).isEqualTo(2);
    }

    @Test
    void 쿠폰_수량이_0일경우_수량감소시_예외() {
        Coupon coupon = Coupon.create(
                "소진된 쿠폰",
                0,
                DiscountPolicy.FLAT,
                BigDecimal.valueOf(1000),
                LocalDate.now().plusDays(1)
        );

        assertThatThrownBy(coupon::decreaseQuantity)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("쿠폰 수량이 부족");
    }

    @Test
    void 쿠폰이_만료되었는지_확인할_수_있다() {
        Coupon expiredCoupon = Coupon.create(
                "만료 쿠폰",
                1,
                DiscountPolicy.FLAT,
                BigDecimal.valueOf(1000),
                LocalDate.now().minusDays(1)
        );

        assertThat(expiredCoupon.isExpired()).isTrue();
    }


    @Test
    void 쿠폰이_할인계산을_정책에_위임한다() {
        Coupon coupon = Coupon.create("10% 할인", 1, DiscountPolicy.RATE,
                BigDecimal.valueOf(10), LocalDate.now().plusDays(1));

        BigDecimal discount = coupon.calculateDiscount(BigDecimal.valueOf(20000));

        assertThat(discount).isEqualByComparingTo("2000");
    }
}
