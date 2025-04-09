package kr.hhplus.be.server.domain.user.pointHistory;

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
}
