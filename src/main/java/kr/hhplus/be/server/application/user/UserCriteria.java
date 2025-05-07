package kr.hhplus.be.server.application.user;

import kr.hhplus.be.server.domain.balance.BalanceCommand;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

public class UserCriteria {

    @Getter
    public static class Create {
        String username;
        BigDecimal point = BigDecimal.ZERO;

        public static Create of(String username){
            return Create.builder().username(username).build();
        }

        public BalanceCommand.Create toBalanceCreateCommand(Long userId){
            return BalanceCommand.Create.of(userId, point);
        }

        @Builder
        private Create(String username) {
            this.username = username;
        }
    }
}
