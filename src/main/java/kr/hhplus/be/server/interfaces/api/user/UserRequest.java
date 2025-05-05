package kr.hhplus.be.server.interfaces.api.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import kr.hhplus.be.server.domain.user.UserCommand;
import lombok.Getter;

public class UserRequest {

    @Getter
    public static class Charge {

        @NotNull @Positive
        private Integer chargePoint;

        public Charge(){}

        public Charge(Integer chargePoint) {
            this.chargePoint = chargePoint;
        }

        public UserCommand.Charge toCommand(Long userId) {
            return UserCommand.Charge.of(userId, chargePoint);
        }
    }
}
