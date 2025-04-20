package kr.hhplus.be.server.domain.coupon;

import java.time.LocalDate;

public class CouponTestFixture {

    public static Coupon createCouponFixtureWithQuantityAndDiscountPrice(Integer quantity, Integer discountPrice) {
        return Coupon.createFlatCoupon(
                "잔여수량 10개 정량할인쿠폰",
                quantity,
                LocalDate.now().plusDays(10),
                discountPrice);
    }

    public static Coupon createTestCouponWithDiscountPrice(Integer discountPrice) {
        return Coupon.createFlatCoupon(
                "test flat coupon",
                9999,
                LocalDate.now().plusYears(999),
                discountPrice
        );
    }

    public static Coupon createTeatCouponWithExpiredDate(LocalDate expiredDate) {
        return new Coupon(
                "test flat coupon already expired",
                CouponType.FLAT,
                999,
                expiredDate,
                expiredDate.minusDays(999));
    }

}
