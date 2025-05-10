package kr.hhplus.be.server.interfaces.api.balance;

import kr.hhplus.be.server.domain.balance.BalanceInfo;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

public class BalanceResponse {

    @Getter
    public static class Charge {

        Long userId;
        BigDecimal nowPoint;

        public static BalanceResponse.Charge from(BalanceInfo.Charge charge) {
            return BalanceResponse.Charge.builder().userId(charge.getUserId()).nowPoint(charge.getNowPoint()).build();
        }

        @Builder
        private Charge(Long userId, BigDecimal nowPoint) {
            this.userId = userId;
            this.nowPoint = nowPoint;
        }
    }


}
