package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.domain.FakeUser;
import kr.hhplus.be.server.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class CouponTest {

    @Nested
    @DisplayName("쿠폰 생성 테스트")
    class coupon_create_test {
        @Test
        void 쿠폰이름이_없을경우_IllegalArgumentException를_반환한다(){
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
        void 쿠폰수량이_없을경우_IllegalArgumentException를_반환한다(){
            assertThrows(IllegalArgumentException.class,
                    () -> Coupon.createFlatCoupon(
                            null,
                            null,
                            LocalDate.now().plusDays(10),
                            5000));
        }

        @Test
        void 쿠폰만료일이_없을경우_IllegalArgumentException를_반환한다(){
            assertThrows(IllegalArgumentException.class,
                    () -> Coupon.createFlatCoupon(
                            "test",
                            10,
                            null,
                            null));
        }

        @ParameterizedTest
        @ValueSource(ints = {-100, -50, -30, -10, -5, -3, -1, 0})
        void 쿠폰수량이_0개이하일경우_IllegalArgumentException를_반환한다(Integer value){
            assertThrows(IllegalArgumentException.class,
                    () -> Coupon.createFlatCoupon(
                            null,
                            value,
                            LocalDate.now().plusDays(10),
                            5000));
        }

        @Test
        void 쿠폰만료일이_지났을경우_IllegalArgumentException를_반환한다(){
            assertThrows(IllegalArgumentException.class,
                    () -> Coupon.createFlatCoupon(
                            "test",
                            111,
                            LocalDate.now().minusDays(10),
                            5000));
        }
    }

    @Nested
    @DisplayName("사용자 쿠폰 발급 테스트")
    class user_coupon_issue_test {
        User user = new FakeUser(1L, "tester");

        Coupon coupon = new Coupon("test", CouponType.FLAT, 10,
                LocalDate.of(2025, 12, 31), LocalDate.of(2025,1,1));

        FlatDiscountCoupon flatCoupon = new FlatDiscountCoupon(coupon, 50_000);

        @Test
        void 사용자쿠폰을_발급한다(){
            UserCoupon userCoupon = coupon.issue(user, LocalDate.of(2025, 1, 1));

            assertEquals(coupon, userCoupon.getCoupon());
        }

        @Test
        void 사용자쿠폰이_발급되면_재고가_차감된다(){
            Coupon coupon = new Coupon("test", CouponType.FLAT, 10,
                    LocalDate.of(2025, 12, 31), LocalDate.of(2025,1,1));
            FlatDiscountCoupon flatCoupon = new FlatDiscountCoupon(coupon, 50_000);

            coupon.issue(user, LocalDate.of(2025, 1, 1));

            assertEquals(9, coupon.getQuantity());
        }
    }

    @Nested
    @DisplayName("쿠폰 할인 적용 테스트")
    class apply_discount_test {
        User user = new FakeUser(1L, "tester");

        Coupon coupon = new Coupon("test", CouponType.FLAT, 10,
                LocalDate.of(2025, 12, 31), LocalDate.of(2025,1,1));

        FlatDiscountCoupon flatCoupon = new FlatDiscountCoupon(coupon, 50_000);

        @Test
        void 쿠폰할인금액이_차감된_금액을_계산한다(){
            Integer discountPrice = coupon.calculateDiscount(100_000);

            assertEquals(50_000, discountPrice);
        }

        @Test
        void 할인금액이_주문총금액보다_많을경우_할인적용금액은_0원이다(){
            Integer discountPrice = coupon.calculateDiscount(10_000);

            assertEquals(0, discountPrice);
        }
    }


    @Test
    void 만료일_이전의_쿠폰여부를_확인한다(){
        //given
        LocalDate expireTime = LocalDate.of(2025, 1,1);
        LocalDate nowDateTime = LocalDate.of(2024, 1,1);
        Coupon coupon = new Coupon("test",CouponType.FLAT, 10, expireTime, nowDateTime);

        //when
        LocalDate trueDateTime = LocalDate.of(2024, 12,31);

        //then
        assertTrue(coupon.isBeforeExpiredDate(trueDateTime));
    }

    @Test
    void 쿠폰재고_보유여부를_확인한다(){
        //given
        Coupon coupon =
                Coupon.createFlatCoupon(
                        "test",
                        1,
                        LocalDate.now().plusDays(10),
                        5000);

        //when
        coupon.decreaseQuantity();

        //then
        assertFalse(coupon.hasEnoughQuantity());
    }

    @Test
    void 쿠폰_잔여수량은_1개_차감한다(){
        //given
        Coupon coupon =
                Coupon.createFlatCoupon(
                        "test",
                        10,
                        LocalDate.now().plusDays(10),
                        5000);


        //when
        coupon.decreaseQuantity();

        //then
        assertEquals(9, coupon.getQuantity());
    }

    @Test
    void 잔여수량이_0개인_쿠폰의경우_차감요청시_IllegalArgumentException를반환한다(){
        //given
        Coupon coupon =
                Coupon.createFlatCoupon(
                        "test",
                        1,
                        LocalDate.now().plusDays(10),
                        5000);

        //when
        coupon.decreaseQuantity();

        assertThrows(IllegalArgumentException.class, () -> coupon.decreaseQuantity());
    }



}
