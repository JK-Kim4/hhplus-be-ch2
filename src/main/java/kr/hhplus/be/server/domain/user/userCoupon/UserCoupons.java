package kr.hhplus.be.server.domain.user.userCoupon;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import kr.hhplus.be.server.domain.coupon.Coupon;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Embeddable @Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class UserCoupons {

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
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
