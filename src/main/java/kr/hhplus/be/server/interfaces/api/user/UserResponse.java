package kr.hhplus.be.server.interfaces.api.user;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserCommand;

public class UserResponse {

    static public class Detail{
        @Schema(name = "userId", description = "사용자 고유번호", example = "1")
        private Long userId;

        @Schema(name = "name", description = "사용자 이름", example = "홍길동")
        private String name;

        @Schema(name = "point", description = "포인트 잔액", example = "15000")
        private Integer point;

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getPoint() {
            return point;
        }

        public void setPoint(Integer point) {
            this.point = point;
        }

        public Detail() {}

        public Detail(Long userId, String name, Integer point) {
            this.userId = userId;
            this.name = name;
            this.point = point;
        }

        public Detail(UserCommand.Response command) {
            User user = command.getUser();
            this.userId = user.getId();
            this.name = user.getName();
            this.point = user.getPoint();
        }
    }

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

        public Point() {}

        public Point(Long userId, Integer point) {
            this.userId = userId;
            this.point = point;
        }

        public Point(UserCommand.Response command) {
            User user = command.getUser();
            this.userId = user.getId();
            this.point = user.getPoint();
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
