package kr.hhplus.be.server.domain.coupon.userCoupon;

import kr.hhplus.be.server.domain.coupon.Coupon;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class UserCoupons {

    private List<UserCoupon> userCoupons = new ArrayList<>();

    public UserCoupons(List<UserCoupon> userCoupons) {
        this.userCoupons = userCoupons;
    }

    public void addUserCoupon(UserCoupon userCoupon) {
        userCoupons.add(userCoupon);
    }

    public boolean isAlreadyIssuedCoupon(Coupon coupon) {
        for (UserCoupon userCoupon : userCoupons) {
            if (userCoupon.getCoupon().equals(coupon)) {
                return true;
            }
        }
        return false;
    }
}
