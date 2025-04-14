package kr.hhplus.be.server.application.user;

public class UserCriteria {


    public static class Charge {

        private Long userId;
        private Integer chargeAmount;

        public static Charge of(Long userId, Integer chargeAmount) {
            return new Charge(userId, chargeAmount);
        }

        private Charge(Long userId, Integer chargeAmount) {
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
