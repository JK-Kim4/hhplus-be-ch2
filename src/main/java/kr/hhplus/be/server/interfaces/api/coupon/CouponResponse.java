package kr.hhplus.be.server.interfaces.api.coupon;

import kr.hhplus.be.server.application.coupon.CouponInfo;
import kr.hhplus.be.server.domain.coupon.CouponType;
import kr.hhplus.be.server.domain.coupon.userCoupon.UserCouponInfo;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class CouponResponse {

    @Getter
    static public class Create {
        private Long couponId;
        private String name;
        private CouponType couponType;
        private float discountRate;
        private Integer discountPrice;
        private Integer remainQuantity;
        private LocalDateTime expireDateTime;

        public Create() {}

        public Create(
                Long couponId, String name,
                CouponType couponType, float discountRate,
                Integer discountPrice, Integer remainQuantity,
                LocalDateTime expireDateTime) {
            this.couponId = couponId;
            this.name = name;
            this.couponType = couponType;
            this.discountRate = discountRate;
            this.discountPrice = discountPrice;
            this.remainQuantity = remainQuantity;
            this.expireDateTime = expireDateTime;
        }

    }

    @Getter
    static public class Issue {

        private Long userCouponId;
        private Long couponId;
        private Long userId;
        private LocalDateTime issueDateTime;


        public Issue(
                Long couponId, Long userId,
                Long userCouponId, LocalDateTime issueDateTime) {
            this.couponId = couponId;
            this.userId = userId;
            this.userCouponId = userCouponId;
            this.issueDateTime = issueDateTime;
        }

        public static Issue from(UserCouponInfo.Issue issue) {
            return new Issue(issue.getCouponId(), issue.getUserId(), issue.getUserCouponId(), issue.getIssueDateTime());
        }
    }

    @Getter
    public static class UserCoupon {

        private Long userCouponId;
        private Long userId;
        private String userName;
        private Long couponId;
        private String couponName;
        private String couponType;
        private LocalDateTime issueDateTime;
        private Long appliedOrderId;
        private LocalDateTime applyDateTime;

        public static UserCoupon from(CouponInfo.UserCouponInfo userCouponInfo){
            UserCoupon userCoupon = new UserCoupon(
                    userCouponInfo.getUserCouponId(),
                    userCouponInfo.getUserId(),
                    userCouponInfo.getUserName(),
                    userCouponInfo.getCouponId(),
                    userCouponInfo.getCouponName(),
                    userCouponInfo.getCouponType(),
                    userCouponInfo.getIssueDateTime()
            );

            if(Objects.nonNull(userCouponInfo.getAppliedOrderId())){
                userCoupon.setAppliedInfo(userCouponInfo.getAppliedOrderId(), userCouponInfo.getApplyDateTime());
            }

            return userCoupon;
        }

        public static List<UserCoupon> fromList(List<CouponInfo.UserCouponInfo> userCouponInfoList){
            return userCouponInfoList.stream().map(UserCoupon::from).toList();
        }

        private UserCoupon(
                Long userCouponId,
                Long userId,
                String userName,
                Long couponId,
                String couponName,
                String  couponType,
                LocalDateTime issueDateTime){
            this.userCouponId = userCouponId;
            this.userId = userId;
            this.userName = userName;
            this.couponId = couponId;
            this.couponName = couponName;
            this.couponType = couponType;
            this.issueDateTime = issueDateTime;
        }

        private void setAppliedInfo(Long appliedOrderId, LocalDateTime applyDateTime){
            this.appliedOrderId = appliedOrderId;
            this.applyDateTime = applyDateTime;
        }
    }

    @Getter
    public static class UserCouponList {

        private UserCouponInfo.UserCouponList userCouponList;

        public static UserCouponList from(UserCouponInfo.UserCouponList userCouponList) {
            return new UserCouponList(userCouponList);
        }

        public UserCouponList(UserCouponInfo.UserCouponList userCouponList) {
            this.userCouponList = userCouponList;
        }
    }
}
