package kr.hhplus.be.server.interfaces.balance.api;

import kr.hhplus.be.server.domain.balance.BalanceInfo;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class BalanceResponse {

    Long userId;
    Long balanceId;
    BigDecimal point;

    public static BalanceResponse from(BalanceInfo balance) {
        return BalanceResponse.builder()
                .userId(balance.getUserId())
                .balanceId(balance.getBalanceId())
                .point(balance.getPoint())
                .build();
    }

    @Builder
    private BalanceResponse(Long userId, Long balanceId, BigDecimal point) {
        this.userId = userId;
        this.balanceId = balanceId;
        this.point = point;
    }

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
