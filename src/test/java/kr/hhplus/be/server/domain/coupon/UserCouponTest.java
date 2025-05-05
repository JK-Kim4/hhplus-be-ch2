package kr.hhplus.be.server.domain.coupon;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserCouponTest {

    @Test
    void 쿠폰을_정상적으로_발급할_수_있다() {
        Coupon coupon = Coupon.create("5천원 할인", 10, DiscountPolicy.FLAT,
                BigDecimal.valueOf(5000), LocalDate.now().plusDays(3));
        UserCoupon userCoupon = UserCoupon.issue(coupon, 1L);

        assertThat(userCoupon.getUserId()).isEqualTo(1L);
        assertThat(userCoupon.getCoupon()).isEqualTo(coupon);
    }

    @Test
    void 정액_할인_쿠폰의_할인금액을_계산할_수_있다() {
        Coupon coupon = Coupon.create("정액", 1, DiscountPolicy.FLAT,
                BigDecimal.valueOf(3000), LocalDate.now().plusDays(1));
        UserCoupon userCoupon = UserCoupon.issue(coupon, 1L);

        BigDecimal discount = userCoupon.calculateDiscountAmount(BigDecimal.valueOf(10000));
        assertThat(discount).isEqualByComparingTo("3000");
    }

    @Test
    void 정률_할인_쿠폰의_할인금액을_계산할_수_있다() {
        Coupon coupon = Coupon.create("정률", 1, DiscountPolicy.RATE,
                BigDecimal.valueOf(10), LocalDate.now().plusDays(1)); // 10%
        UserCoupon userCoupon = UserCoupon.issue(coupon, 1L);

        BigDecimal discount = userCoupon.calculateDiscountAmount(BigDecimal.valueOf(20000));
        assertThat(discount).isEqualByComparingTo("2000"); // 10% of 20000
    }

    @Test
    void 이미_사용된_쿠폰은_할인금액_계산시_예외발생() {
        Coupon coupon = Coupon.create("사용됨", 1, DiscountPolicy.FLAT,
                BigDecimal.valueOf(1000), LocalDate.now().plusDays(1));
        UserCoupon userCoupon = UserCoupon.issue(coupon, 1L);
        userCoupon.use(); // 사용 처리

        assertThatThrownBy(() -> userCoupon.calculateDiscountAmount(BigDecimal.valueOf(10000)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 사용된 쿠폰");
    }

    @Test
    void 만료된_쿠폰은_할인금액_계산시_예외발생() {
        Coupon coupon = Coupon.create("만료됨", 1, DiscountPolicy.FLAT,
                BigDecimal.valueOf(1000), LocalDate.now().minusDays(1)); // expired
        UserCoupon userCoupon = UserCoupon.issue(coupon, 1L);

        assertThatThrownBy(() -> userCoupon.calculateDiscountAmount(BigDecimal.valueOf(10000)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("만료된 쿠폰");
    }
}
