package kr.hhplus.be.server.application.user;

import kr.hhplus.be.server.domain.user.UserCommand;

public class UserCriteria {


    public static class Charge {

        private Long userId;
        private Integer chargeAmount;

        public UserCommand.Charge toCommand() {
            return new UserCommand.Charge(userId, chargeAmount);
        }

        public Charge(Long userId, Integer chargeAmount) {
            this.userId = userId;
            this.chargeAmount = chargeAmount;
        }

        public Long getUserId() {
            return userId;
        }

        public Integer getChargeAmount() {
            return chargeAmount;
        }
    }
}
