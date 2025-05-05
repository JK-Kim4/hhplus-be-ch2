package kr.hhplus.be.server.domain.balance;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

public class BalanceInfo {

    @Getter
    public static class Create {

        Long userId;
        BigDecimal point;

        public static Create from(Balance balance) {
            return Create.builder().userId(balance.getUserId()).point(balance.getPoint().getAmount()).build();
        }

        @Builder
        private Create(Long userId, BigDecimal point) {
            this.userId = userId;
            this.point = point;
        }

    }

    @Getter
    public static class Charge {
        Long userId;
        BigDecimal nowPoint;

        public static Charge from(Balance balance) {
            return Charge.builder().userId(balance.getUserId()).point(balance.getPoint().getAmount()).build();
        }

        @Builder
        private Charge(Long userId, BigDecimal point) {
            this.userId = userId;
            this.nowPoint = point;
        }
    }

    @Getter
    public static class Deduct {
        Long userId;
        BigDecimal nowPoint;

        public static Deduct from(Balance balance) {
            return Deduct.builder().userId(balance.getUserId()).point(balance.getPoint().getAmount()).build();
        }

        @Builder
        private Deduct(Long userId, BigDecimal point) {
            this.userId = userId;
            this.nowPoint = point;
        }
    }
}
