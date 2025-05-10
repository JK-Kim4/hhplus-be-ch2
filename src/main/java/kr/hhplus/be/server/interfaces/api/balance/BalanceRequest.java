package kr.hhplus.be.server.interfaces.api.balance;

import kr.hhplus.be.server.domain.balance.BalanceCommand;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

public class BalanceRequest {

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
