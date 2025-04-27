package kr.hhplus.be.server.domain.user;

import lombok.Getter;

public class UserInfo {

    @Getter
    public static class Point {

        private Long userId;
        private Integer amount;

        public static Point of(Long userId, Integer amount) {
            return new Point(userId, amount);
        }

        public static Point from(User user) {
            return new Point(user.getId(), user.point());
        }

        private Point(Long userId, Integer amount) {
            this.userId = userId;
            this.amount = amount;
        }

    }
}
