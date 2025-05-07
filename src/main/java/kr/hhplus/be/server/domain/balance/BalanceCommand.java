package kr.hhplus.be.server.domain.balance;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

public class BalanceCommand {


    @Getter
    public static class Create {
        Long userId;
        BigDecimal point;

        public static Create of(Long userId, BigDecimal point) {
            return new Create(userId, point);
        }

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

        public static Charge of(Long userId, BigDecimal chargePoint) {
            return new Charge(userId, chargePoint);
        }

        @Builder
        private Charge(Long userId, BigDecimal chargePoint) {
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
