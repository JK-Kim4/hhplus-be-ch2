package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.domain.coupon.userCoupon.UserCoupon;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class CouponInfo {

    @Getter
    public static class UserCouponInfo {

        protected Long userCouponId;
        private Long userId;
        private String userName;
        private Long couponId;
        private String couponName;
        private String couponType;
        private Long appliedOrderId;
        protected LocalDateTime issueDateTime;
        protected LocalDateTime applyDateTime;

        public static UserCouponInfo from(UserCoupon userCoupon){
            UserCouponInfo userCouponInfo = new UserCouponInfo(
                    userCoupon.getId(),
                    userCoupon.getUser().getId(),
                    userCoupon.getUser().getName(),
                    userCoupon.getCoupon().getId(),
                    userCoupon.getCoupon().getName(),
                    userCoupon.getCoupon().getCouponType().name(),
                    userCoupon.getIssueDateTime(),
                    userCoupon.getApplyDateTime());

            if(Objects.nonNull(userCouponInfo.getAppliedOrderId())){
                userCouponInfo.setAppliedInfo(userCouponInfo.getAppliedOrderId(), userCouponInfo.getApplyDateTime());
            }

            return userCouponInfo;
        }

        private UserCouponInfo(
                Long userCouponId,
                Long userId,
                String userName,
                Long couponId,
                String couponName,
                String couponType,
                LocalDateTime issueDateTime,
                LocalDateTime applyDateTime) {
            this.userCouponId = userCouponId;
            this.userId = userId;
            this.userName = userName;
            this.couponId = couponId;
            this.couponName = couponName;
            this.couponType = couponType;
            this.issueDateTime = issueDateTime;
            this.applyDateTime = applyDateTime;
        }

        public static List<UserCouponInfo> fromList(List<UserCoupon> userCouponList){
            return userCouponList.stream().map(UserCouponInfo::from).toList();
        }

        private void setAppliedInfo(
                Long appliedOrderId, LocalDateTime applyDateTime) {
            this.appliedOrderId = appliedOrderId;
            this.applyDateTime = applyDateTime;
        }

    }
}
