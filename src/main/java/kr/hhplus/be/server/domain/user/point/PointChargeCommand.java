package kr.hhplus.be.server.domain.user.point;

public class PointChargeCommand {

    private Long userId;
    private Integer amount;

    public PointChargeCommand(Long userId, Integer amount) {
        this.userId = userId;
        this.amount = amount;
    }

    public Long getUserId() {
        return userId;
    }

    public Integer getAmount() {
        return amount;
    }

    public static class Response {
        private Integer point;

        public Response(Integer point) {
            this.point = point;
        }

        public Integer getPoint() {
            return point;
        }
    }
}
