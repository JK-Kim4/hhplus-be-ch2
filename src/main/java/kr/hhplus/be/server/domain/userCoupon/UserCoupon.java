package kr.hhplus.be.server.domain.userCoupon;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCoupon {

    private Long id;
    private User user;
    private Coupon coupon;
    private boolean isUsed;
    private LocalDateTime issueDateTime;

    public UserCoupon(User user, Coupon coupon) {
        this.user = user;
        this.coupon = coupon;
        this.isUsed = false;
        this.issueDateTime = LocalDateTime.now();
    }

    public void isUsable(LocalDateTime targetDateTime) {
        coupon.isUsable(targetDateTime);

        if(isUsed){
            throw new IllegalArgumentException("이미 사용된 쿠폰입니다.");
        }
    }

    public boolean isCouponOwner(Long userId) {
        return userId.equals(this.user.getId());
    }

    public Integer discount(Integer price) {
        return coupon.discount(price);
    }

    public void updateUsedFlag(boolean value) {
        this.isUsed = value;
    }
}
