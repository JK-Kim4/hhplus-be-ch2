package kr.hhplus.be.server.domain.point;

public class PointInfo {

    public static class Point {
        private Long userId;
        private Integer amount;

        public static Point from(kr.hhplus.be.server.domain.point.Point point){
            return new Point(point);
        }

        public static Point of(Long userId, Integer amount) {
            return new Point(userId, amount);
        }

        private Point(kr.hhplus.be.server.domain.point.Point point) {
            this.userId = point.getId();
            this.amount = point.getAmount();
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
