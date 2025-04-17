package kr.hhplus.be.server.domain.couponv2;

import kr.hhplus.be.server.domain.FakeUser;
import kr.hhplus.be.server.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class CouponV2Test {

    @Nested
    @DisplayName("쿠폰 생성 테스트")
    class couponv2_create_test {
        @Test
        void 쿠폰이름이_없을경우_쿠폰이름이_없을경우_IllegalArgumentException를_반환한다(){
            assertThrows(IllegalArgumentException.class,
                    () -> new CouponV2(null, CouponType.FLAT, 10, LocalDate.now().plusDays(10)));

            assertThrows(IllegalArgumentException.class,
                    () -> new CouponV2("",CouponType.FLAT, 10, LocalDate.now().plusDays(10)));

            assertThrows(IllegalArgumentException.class,
                    () -> new CouponV2(" ",CouponType.FLAT, 10, LocalDate.now().plusDays(10)));
        }

        @Test
        void 쿠폰수량이_없을경우_쿠폰이름이_없을경우_IllegalArgumentException를_반환한다(){
            assertThrows(IllegalArgumentException.class,
                    () -> new CouponV2("test",CouponType.FLAT, null, LocalDate.now().plusDays(10)));
        }

        @Test
        void 쿠폰만료일이_없을경우_쿠폰이름이_없을경우_IllegalArgumentException를_반환한다(){
            assertThrows(IllegalArgumentException.class,
                    () -> new CouponV2("test",CouponType.FLAT, 10, null));
        }

        @Test
        void 쿠폰수량이_0개이하일경우_쿠폰이름이_없을경우_IllegalArgumentException를_반환한다(){


            assertThrows(IllegalArgumentException.class,
                    () -> new CouponV2("test",CouponType.FLAT, 0, LocalDate.now().plusDays(10)));
        }

        @Test
        void 쿠폰만료일이_지났을경우_IllegalArgumentException를_반환한다(){
            assertThrows(IllegalArgumentException.class,
                    () -> new CouponV2("test",CouponType.FLAT, 10, LocalDate.now().minusDays(10)));
        }
    }

    @Nested
    @DisplayName("사용자 쿠폰 발급 테스트")
    class user_coupon_issue_test {
        User user = new FakeUser(1L, "tester");

        CouponV2 coupon = new CouponV2("test", CouponType.FLAT, 10,
                LocalDate.of(2025, 12, 31), LocalDate.of(2025,1,1));

        FlatDiscountCouponV2 flatCoupon = new FlatDiscountCouponV2(coupon, 50_000);

        @Test
        void 사용자쿠폰을_발급한다(){
            UserCouponV2 userCoupon = coupon.issueUserCoupon(user, LocalDate.of(2025, 1, 1));

            assertEquals(coupon, userCoupon.getCoupon());
        }

        @Test
        void 사용자쿠폰이_발급되면_재고가_차감된다(){
            CouponV2 coupon = new CouponV2("test", CouponType.FLAT, 10,
                    LocalDate.of(2025, 12, 31), LocalDate.of(2025,1,1));
            FlatDiscountCouponV2 flatCoupon = new FlatDiscountCouponV2(coupon, 50_000);

            coupon.issueUserCoupon(user, LocalDate.of(2025, 1, 1));

            assertEquals(9, coupon.getQuantity());
        }
    }

    @Nested
    @DisplayName("쿠폰 할인 적용 테스트")
    class apply_discount_test {
        User user = new FakeUser(1L, "tester");

        CouponV2 coupon = new CouponV2("test", CouponType.FLAT, 10,
                LocalDate.of(2025, 12, 31), LocalDate.of(2025,1,1));

        FlatDiscountCouponV2 flatCoupon = new FlatDiscountCouponV2(coupon, 50_000);

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
        CouponV2 coupon = new CouponV2("test",CouponType.FLAT, 10, expireTime, nowDateTime);

        //when
        LocalDate trueDateTime = LocalDate.of(2024, 12,31);

        //then
        assertTrue(coupon.isBeforeExpiredDate(trueDateTime));
    }

    @Test
    void 쿠폰재고_보유여부를_확인한다(){
        //given
        CouponV2 coupon = new CouponV2("test",CouponType.FLAT, 1, LocalDate.now().plusDays(10));

        //when
        coupon.decreaseQuantity();

        //then
        assertFalse(coupon.hasEnoughQuantity());
    }

    @Test
    void 쿠폰_잔여수량은_1개_차감한다(){
        //given
        CouponV2 coupon = new CouponV2("test",CouponType.FLAT, 10, LocalDate.now().plusDays(10));

        //when
        coupon.decreaseQuantity();

        //then
        assertEquals(9, coupon.getQuantity());
    }

    @Test
    void 잔여수량이_0개인_쿠폰의경우_차감요청시_IllegalArgumentException를반환한다(){
        //given
        CouponV2 coupon = new CouponV2("test",CouponType.FLAT, 1, LocalDate.now().plusDays(10));

        //when
        coupon.decreaseQuantity();

        assertThrows(IllegalArgumentException.class, () -> coupon.decreaseQuantity());
    }



}
