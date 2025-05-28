package kr.hhplus.be.server.interfaces.balance.api;

import kr.hhplus.be.server.domain.balance.BalanceCommand;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class BalanceRequest {

    private Long userId;
    private Long balanceId;

    public BalanceCommand toCommand() {
        return BalanceCommand.of(userId, balanceId);
    }

    @Builder
    public BalanceRequest(Long userId) {
        this.userId = userId;
    }

    @Getter
    public static class Charge {
        Long userId;
        BigDecimal chargePoint;

        public BalanceCommand.Charge toCommand() {
            return BalanceCommand.Charge.of(userId, chargePoint);
        }

        @Builder
        private Charge(Long userId, BigDecimal chargePoint) {
            this.userId = userId;
            this.chargePoint = chargePoint;
        }
    }
}
