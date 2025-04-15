package kr.hhplus.be.server.application.user;

import kr.hhplus.be.server.domain.user.UserInfo;

public class UserResult {


    public static class User {

        private Long userId;
        private String name;

        public static User from(UserInfo.User userV2Info) {
            return new User(userV2Info.getUserId(), userV2Info.getName());
        }

        private User(Long userId, String name) {
            this.userId = userId;
            this.name = name;
        }

        public Long getUserId() {
            return userId;
        }

        public String getName() {
            return name;
        }
    }

    public static class Point {

        private Long userId;
        private Integer amount;

        public static Point from(kr.hhplus.be.server.domain.point.Point point) {
            return new Point(point.getUserId(), point.getAmount());
        }

        private Point(Long userId, Integer amount) {
            this.userId = userId;
            this.amount = amount;
        }

        public Long getUserId() {
            return userId;
        }

        public Integer getAmount() {
            return amount;
        }
    }
}
