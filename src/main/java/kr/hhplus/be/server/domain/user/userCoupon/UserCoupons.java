package kr.hhplus.be.server.domain.user.userCoupon;

import kr.hhplus.be.server.domain.couponv2.CouponV2;
import kr.hhplus.be.server.domain.couponv2.UserCouponV2;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class UserCoupons {

    private List<UserCouponV2> userCoupons = new ArrayList<>();

    public UserCoupons(List<UserCouponV2> userCoupons) {
        this.userCoupons = userCoupons;
    }

    public void addUserCoupon(UserCouponV2 userCoupon) {
        userCoupons.add(userCoupon);
    }

    public boolean isAlreadyIssuedCoupon(CouponV2 coupon) {
        for (UserCouponV2 userCoupon : userCoupons) {
            if (userCoupon.getCoupon().equals(coupon)) {
                return true;
            }
        }
        return false;
    }
}
