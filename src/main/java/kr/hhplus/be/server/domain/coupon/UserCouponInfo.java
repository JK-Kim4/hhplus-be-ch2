package kr.hhplus.be.server.domain.coupon;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public class UserCouponInfo {

    public static class UserCouponList{

        private List<UserCoupon> userCouponList;

        public List<UserCoupon> getUserCouponList() {
            return userCouponList;
        }

        public static UserCouponList of(List<UserCoupon> list){
            return new UserCouponList(list);
        }

        public UserCouponList(List<UserCoupon> userCouponList) {
            this.userCouponList = userCouponList;
        }

    }

    @Getter
    public static class Issue {

        private Long userCouponId;
        private Long couponId;
        private Long userId;
        private LocalDateTime issueDateTime;

        public static Issue of(
                Long userCouponId,
                Long couponId,
                Long userId,
                LocalDateTime issueDateTime) {
            return new Issue(userCouponId, couponId, userId, issueDateTime);
        }

        public Issue(
                Long userCouponId,
                Long couponId,
                Long userId,
                LocalDateTime issueDateTime) {
            this.userCouponId = userCouponId;
            this.couponId = couponId;
            this.userId = userId;
            this.issueDateTime = issueDateTime;
        }

    }
}
