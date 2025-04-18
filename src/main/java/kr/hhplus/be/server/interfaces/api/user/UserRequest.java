package kr.hhplus.be.server.interfaces.api.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import kr.hhplus.be.server.application.user.UserCriteria;

public class UserRequest {

    public static class Charge {

        @NotNull @Positive
        private Integer chargePoint;

        public UserCriteria.Charge toCriteria(Long userId) {
            return new UserCriteria.Charge(userId, chargePoint);
        }

        public Charge(Integer chargePoint) {
            this.chargePoint = chargePoint;
        }

        public Integer getChargePoint() {
            return chargePoint;
        }
    }
}
