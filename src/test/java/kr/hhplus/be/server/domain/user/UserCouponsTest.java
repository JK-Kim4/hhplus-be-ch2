package kr.hhplus.be.server.domain.user;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.user.userCoupon.UserCoupon;
import kr.hhplus.be.server.domain.user.userCoupon.UserCoupons;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;

public class UserCouponsTest {

    @Test
    void 사용자쿠폰_목록으로_userCoupons를_생성한다(){
        User user = mock(User.class);
        Coupon coupon = mock(Coupon.class);;
        FakeCoupon userCoupons = new FakeCoupon(user, coupon);

        new UserCoupons(List.of(userCoupons));
    }

    @Test
    void 쿠폰을_추가할수있다(){
        User user = mock(User.class);
        Coupon coupon = mock(Coupon.class);;
        FakeCoupon fakeCoupon = new FakeCoupon(user, coupon);
        List<UserCoupon> coponList = new ArrayList<>();
        coponList.add(fakeCoupon);

        UserCoupons userCoupons = new UserCoupons(coponList);

        userCoupons.addUserCoupon(fakeCoupon);
    }

    @Test
    void 이미_발급받은_쿠폰을_추가로_발급할수없다(){
        User user = mock(User.class);
        Coupon coupon = mock(Coupon.class);;
        FakeCoupon fakeCoupon = new FakeCoupon(user, coupon);
        List<UserCoupon> coponList = new ArrayList<>();
        coponList.add(fakeCoupon);

        UserCoupons userCoupons = new UserCoupons(coponList);

        Assertions.assertTrue(userCoupons.isAlreadyIssuedCoupon(coupon));
    }


    class FakeCoupon extends UserCoupon {

        public FakeCoupon(User user, Coupon coupon) {
            super(user, coupon);
        }

        @Override
        public void createValidation(User user, Coupon coupon) {
            return;
        }
    }

}
