package kr.hhplus.be.server.domain.coupon.userCoupon;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class UserCouponInfo {

    @Getter
    public static class UserCouponList{

        private List<Content> userCouponList;

        public List<Content> getUserCouponList() {
            return userCouponList;
        }

        public static UserCouponList of(List<UserCoupon> list){
            return new UserCouponList(list);
        }

        public UserCouponList(List<UserCoupon> userCouponList) {
            this.userCouponList = userCouponList.stream()
                    .map(Content::new).collect(Collectors.toList());
        }

        @Getter
        public static class Content {

            Long userCouponId;
            Long couponId;
            Long userId;
            Long appliedOrderId;
            LocalDateTime issueDateTime;
            LocalDateTime applyDateTime;

            private Content(UserCoupon userCoupon){
                this.userCouponId = userCoupon.getId();
                this.couponId = userCoupon.getCoupon().getId();
                this.userId = userCoupon.getUser().getId();
                this.issueDateTime = userCoupon.getIssueDateTime();
                if(userCoupon.getAppliedOrder() != null){
                    this.appliedOrderId = userCoupon.getAppliedOrder().getId();
                    this.applyDateTime = userCoupon.getApplyDateTime();
                }
            }

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
