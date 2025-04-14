package kr.hhplus.be.server.interfaces.api.user;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.hhplus.be.server.application.user.UserResult;

public class UserResponse {

    static public class Point{

        @Schema(name = "userId", description = "사용자 고유번호", example = "10")
        private Long userId;

        @Schema(name = "point", description = "현재 포인트 잔액", example = "20000")
        private Integer point;

        public Long getUserId() {
            return userId;
        }

        public Integer getPoint() {
            return point;
        }

        public static Point from(UserResult.Point point){
            return new Point(point.getUserId(), point.getAmount());
        }

        private Point() {}

        private Point(Long userId, Integer point) {
            this.userId = userId;
            this.point = point;
        }
    }

    static public class Coupon{

        @Schema(name = "userId", description = "사용자 고유번호", example = "1")
        private Long userId;

        @Schema(name = "userCouponId", description = "사용자 쿠폰 고유번호", example = "9")
        private Long userCouponId;

        @Schema(name = "isUsed", description = "쿠폰 사용 여부", example = "false")
        private Boolean isUsed;

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Long getUserCouponId() {
            return userCouponId;
        }

        public void setUserCouponId(Long userCouponId) {
            this.userCouponId = userCouponId;
        }

        public Boolean getUsed() {
            return isUsed;
        }

        public void setUsed(Boolean used) {
            isUsed = used;
        }

        public Coupon() {}

        public Coupon(Long userId, Long userCouponId, Boolean isUsed) {
            this.userId = userId;
            this.userCouponId = userCouponId;
            this.isUsed = isUsed;
        }
    }

}
