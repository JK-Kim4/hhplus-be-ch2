package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.userCoupon.UserCoupon;

import java.time.LocalDateTime;

public class RateDiscountCoupon extends Coupon {

    private Integer discountRate;

    @Override
    public boolean validate(LocalDateTime targetDateTime) {
        return false;
    }

    @Override
    public UserCoupon issue(User user) {
        return null;
    }

    @Override
    public Integer discount(Integer integer) {
        return 0;
    }
}
