package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.userCoupon.UserCoupon;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class FlatDiscountCouponTest {

    int initAmount = 5;

    CouponTemplate couponTemplate = CouponTemplate.builder()
            .id(10L)
            .name("flatCoupon")
            .couponType(CouponType.FLAT)
            .remainingQuantity(initAmount)
            .expireDateTime(LocalDateTime.of(2099, 1, 1, 0, 0))
            .build();

    User user = new User("tester");
    Integer discountAmount = 15000;
    FlatDiscountCoupon flatCoupon = new FlatDiscountCoupon(couponTemplate, discountAmount);


    @Test
    @DisplayName("정액할인 쿠폰 생성")
    void create_flat_coupon() {
        assertEquals(couponTemplate.getName(), flatCoupon.getName());
        assertEquals(discountAmount, flatCoupon.getDiscountAmount());
    }

    @ParameterizedTest
    @ValueSource(ints = {1999, 2025, 2035, 2045, 2055, 2065, 2075, 2090, 2097})
    @DisplayName("유효 쿠폰 만료일 검증")
    void coupon_expired_date_validation_success(Integer year) {
        assertTrue(flatCoupon.validate(LocalDateTime.of(year, 1, 1, 0, 0)));
    }

    @ParameterizedTest
    @ValueSource(ints = {2100, 2100, 2110, 2300, 2999, 3000, 3100, 5000, 9999})
    @DisplayName("만료 쿠폰 만료일 검증")
    void coupon_expired_date_validation_fail(Integer year) {
        assertFalse(flatCoupon.validate(LocalDateTime.of(year, 1, 1, 0, 0)));
    }

    @Test
    @DisplayName("잔여 수량 & 쿠폰 만료일 검증")
    void coupon_expired_date_remaining_quantity_validation_success() {
        CouponTemplate couponTemplate = CouponTemplate.builder()
                .id(10L)
                .name("flatCoupon")
                .couponType(CouponType.FLAT)
                .remainingQuantity(0)
                .expireDateTime(LocalDateTime.of(2099, 1, 1, 0, 0))
                .build();

        FlatDiscountCoupon flatCoupon = new FlatDiscountCoupon(couponTemplate, discountAmount);
        assertFalse(flatCoupon.validate(LocalDateTime.of(1000, 1, 1, 0, 0)));
    }

    @Test
    @DisplayName("쿠폰 발급시 잔여 수량이 차감된다.")
    void coupon_issue_test(){
        flatCoupon.issue(user);
        assertEquals(initAmount-1, flatCoupon.getRemainingQuantity());
    }

    @Test
    @DisplayName("쿠폰 발급 요청 성공 시 UserCoupon을 생성한다.")
    void coupon_issue_test_(){
        UserCoupon userCoupon = flatCoupon.issue(user);

        assertEquals(userCoupon.getUser(), user);
        assertEquals(userCoupon.getCoupon(), flatCoupon);
    }

    @ParameterizedTest
    @ValueSource(ints = {15_000, 20_000, 50_000})
    @DisplayName("유효한 쿠폰일 경우 총 금액에 대한 할인 적용 금액을 반환한다.")
    void coupon_use_test(Integer price){
        UserCoupon userCoupon = flatCoupon.issue(user);

        Integer finalPrice = userCoupon.discount(price);

        assertEquals(price-discountAmount, finalPrice);
    }

    @ParameterizedTest
    @ValueSource(ints = {11000, 0, -10000})
    @DisplayName("쿠폰 적용 최종 금액이 0 이하일 경우 0을 반환한다.")
    void coupon_use_test_negative(Integer price){
        UserCoupon userCoupon = flatCoupon.issue(user);

        Integer finalPrice = userCoupon.discount(price);

        assertEquals(0, finalPrice);
    }
}
