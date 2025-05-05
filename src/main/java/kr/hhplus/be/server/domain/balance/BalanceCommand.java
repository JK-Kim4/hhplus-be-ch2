package kr.hhplus.be.server.domain.balance;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

public class BalanceCommand {


    @Getter
    public static class Create {
        Long userId;
        BigDecimal point;

        @Builder
        public Create(Long userId, BigDecimal point) {
            this.userId = userId;
            this.point = point;
        }
    }

    @Getter
    public static class Charge {
        Long userId;
        BigDecimal chargePoint;

        @Builder
        public Charge(Long userId, BigDecimal chargePoint) {
            this.userId = userId;
            this.chargePoint = chargePoint;
        }
    }

    @Getter
    public static class Deduct {
        Long userId;
        BigDecimal deductPoint;

        @Builder
        public Deduct(Long userId, BigDecimal deductPoint) {
            this.userId = userId;
            this.deductPoint = deductPoint;
        }


    }
}
