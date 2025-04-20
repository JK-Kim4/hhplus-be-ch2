package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderTestFixture;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserTestFixture;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserCouponTest {

    @Nested
    class 사용자쿠폰_생성{

        @Test
        void 사용자_정보가_없을경우_사용자쿠폰을_생성할수없다(){
            assertThrows(IllegalArgumentException.class, () ->
                    UserCoupon.create(
                            null,
                            CouponTestFixture.createTestCouponWithDiscountPrice(5_000)
                    ));
        }

        @Test
        void 쿠폰_정보가_없을경우_사용자쿠폰을_생성할수없다(){
            assertThrows(IllegalArgumentException.class, () ->
                    UserCoupon.create(
                            UserTestFixture.createTestUser(),
                            null
                    ));
        }
    }

    @Test
    void 쿠폰_발급_사용자와_전달받은_사용자가_일치할경우_isCouponOwner는_true를_반환한다(){
        //given
        User testUser = UserTestFixture.createTestUser();
        UserCoupon userCoupon = UserCoupon.create(testUser, CouponTestFixture.createTestCouponWithDiscountPrice(5_000));

        //when//then
        assertTrue(userCoupon.isCouponOwner(testUser));
    }

    @Test
    void 주문정보를_전달받아_사용자쿠폰의_적용주문정보와_적용일시를_초기화한다(){
        //given
        Order order = OrderTestFixture.createTestOrder();
        UserCoupon userCoupon = UserCoupon.create(UserTestFixture.createTestUser(), CouponTestFixture.createTestCouponWithDiscountPrice(5_000));

        //when
        assertNull(userCoupon.getAppliedOrder());
        userCoupon.updateUsedCouponInformation(order);

        //then
        assertEquals(order, userCoupon.getAppliedOrder());
        assertNotNull(userCoupon.getApplyDateTime());
    }

    @Test
    void 전달받은_날짜가_쿠폰_만료일_이후일경우_isUsable은_IllegalArgumentException(){
        //given
        User user = UserTestFixture.createTestUser();
        Coupon coupon = CouponTestFixture.createTeatCouponWithExpiredDate(LocalDate.of(2025,1,1));
        LocalDate overDue = LocalDate.of(2025,1,2);

        //when
        UserCoupon userCoupon = UserCoupon.create(user, coupon);

        //then
        assertThrows(IllegalArgumentException.class, ()
                -> userCoupon.isUsable(overDue, user));
    }

    @Test
    void 전달받은_사용자가_쿠폰발급_사용자정보와_일치하지않을경우_isUsable은_IllegalArgumentException(){
        //given
        User auser = UserTestFixture.createTestUserWithIdAndName(1L, "test user a");
        User buser = UserTestFixture.createTestUserWithIdAndName(99L, "test user b");
        LocalDate expiredDate = LocalDate.of(2025,1,1);
        Coupon coupon = CouponTestFixture.createTeatCouponWithExpiredDate(expiredDate);

        //when
        UserCoupon userCoupon = UserCoupon.create(auser, coupon);

        //then
        assertThrows(IllegalArgumentException.class, ()
                -> userCoupon.isUsable(expiredDate.minusDays(10), buser));
    }

}
