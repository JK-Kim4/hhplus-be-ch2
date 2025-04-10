package kr.hhplus.be.server.domain.userCoupon;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.user.User;

import java.time.LocalDateTime;

public class UserCoupon {

    private Long id;
    private User user;
    private Coupon coupon;
    private LocalDateTime issueDateTime;

    public Integer calculateDiscountPrice(){

        return null;
    }

}
