package kr.hhplus.be.server.domain.coupon.userCoupon;

public class UserCouponIssueCommand {

    private Long couponId;
    private Long userId;

    public UserCouponIssueCommand(Long couponId, Long userId) {
        this.couponId = couponId;
        this.userId = userId;
    }

    public static UserCouponIssueCommand of(Long couponId, Long userId) {
        return new UserCouponIssueCommand(couponId, userId);
    }

    public Long getCouponId() {
        return couponId;
    }

    public Long getUserId() {
        return userId;
    }


    public static class Response{

        private UserCoupon coupon;

        public Response(UserCoupon coupon) {
            this.coupon = coupon;
        }

        public UserCoupon getUserCoupon() {
            return coupon;
        }
    }
}
