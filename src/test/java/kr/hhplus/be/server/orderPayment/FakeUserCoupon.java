package kr.hhplus.be.server.orderPayment;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.userCoupon.UserCoupon;

public class FakeUserCoupon extends UserCoupon {

    public FakeUserCoupon(Long id, User user, Coupon coupon) {
        super(user, coupon);
        this.id = id;
    }
}
