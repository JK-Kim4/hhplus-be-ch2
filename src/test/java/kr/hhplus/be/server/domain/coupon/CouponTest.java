package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.domain.user.UserTestFixture;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CouponTest {

    @Nested
    class 정량할인_쿠폰_생성 {
        @Test
        void 쿠폰명이_비어있는상태에서_쿠폰을_생성할경우_IllegalArgumentException() {
            assertThrows(IllegalArgumentException.class,
                    () -> Coupon.createFlatCoupon(
                            null,
                            10,
                            LocalDate.now().plusDays(10),
                            5000));

            assertThrows(IllegalArgumentException.class,
                    () -> Coupon.createFlatCoupon(
                            "",
                            10,
                            LocalDate.now().plusDays(10),
                            5000));

            assertThrows(IllegalArgumentException.class,
                    () -> Coupon.createFlatCoupon(
                            " ",
                            10,
                            LocalDate.now().plusDays(10),
                            5000));
        }

        @Test
        void 쿠폰수량이_비어있는상태에서_쿠폰을_생성할경우_IllegalArgumentException() {
            assertThrows(IllegalArgumentException.class,
                    () -> Coupon.createFlatCoupon(
                            "테스트",
                            null,
                            LocalDate.now().plusDays(10),
                            5000));
        }

        @Test
        void 쿠폰만료일이_비어있는상태에서_쿠폰을_생성할경우_IllegalArgumentException() {
            assertThrows(IllegalArgumentException.class,
                    () -> Coupon.createFlatCoupon(
                            "test",
                            10,
                            null,
                            null));
        }

        @ParameterizedTest
        @ValueSource(ints = {-100, -50, -30, -10, -5, -3, -1, 0})
        void 정수0이하의_수량으로_쿠폰을_생성할경우_IllegalArgumentException(Integer value) {
            assertThrows(IllegalArgumentException.class,
                    () -> Coupon.createFlatCoupon(
                            "test",
                            value,
                            LocalDate.now().plusDays(10),
                            5000));
        }

        @Test
        void 현재_시점_이전으로_만료일설정하여_쿠폰을_생성할경우_IllegalArgumentException() {
            assertThrows(IllegalArgumentException.class,
                    () -> Coupon.createFlatCoupon(
                            "test",
                            111,
                            LocalDate.now().minusDays(10),
                            5000));
        }
    }

    @Nested
    class 정량할인_쿠폰_발급{

        @Test
        void 쿠폰_잔여수량이_존재할때_쿠폰_발급에_성공할경우_재고가_1개_차감된다() {
            Coupon coupon = CouponTestFixture.createCouponFixtureWithQuantityAndDiscountPrice(10, 5_000);

            coupon.issue(UserTestFixture.createTestUser(), LocalDate.now());

            assertEquals(9, coupon.getQuantity());
        }

        @Test
        void 잔여수량이_0개인_쿠폰_발급_요청시_IllegalArgumentException() {
            //given
            Coupon coupon = CouponTestFixture.createCouponFixtureWithQuantityAndDiscountPrice(1, 5_000);

            //when
            coupon.decreaseQuantity();

            //then
            assertThrows(IllegalArgumentException.class,
                    () -> coupon.issue(UserTestFixture.createTestUser(), LocalDate.now()));
        }
    }

    @Nested
    class 쿠폰_할인금액_계산{

        @Test
        void 상품의_총가격을_전달받아_할인금액이_차감된_최종금액을_반환한다() {
            Coupon coupon = CouponTestFixture.createCouponFixtureWithQuantityAndDiscountPrice(10, 50_000);

            Integer discountPrice = coupon.calculateDiscount(100_000);

            assertEquals(50_000, discountPrice);
        }

        @Test
        void 상품의_총가격이_할인금액보다_적을경우_차감된_최종금액_0원은_반환한다() {
            Coupon coupon = CouponTestFixture.createCouponFixtureWithQuantityAndDiscountPrice(10, 50_000);

            Integer discountPrice = coupon.calculateDiscount(10_000);

            assertEquals(0, discountPrice);
        }

    }
}
