package kr.hhplus.be.server.interfaces.api.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import kr.hhplus.be.server.domain.user.UserCommand;

public class UserRequest {

    public static class Charge {

        @NotNull @Positive
        private Integer chargePoint;

        public Charge(Integer chargePoint) {
            this.chargePoint = chargePoint;
        }

        public Integer getChargePoint() {
            return chargePoint;
        }

        public UserCommand.Charge toCommand(Long userId) {
            return UserCommand.Charge.of(userId, chargePoint);
        }
    }
}
