package kr.hhplus.be.server.domain.user;

import lombok.Getter;

public class UserCommand {

    @Getter
    public static class Charge {

        private Long userId;
        private Integer amount;

        public Charge() {}

        public static Charge of(Long userId, Integer amount) {
            return new Charge(userId, amount);
        }

        private Charge(Long userId, Integer amount) {
            this.userId = userId;
            this.amount = amount;
        }
    }


    @Getter
    public static class Deduct {

        private Long userId;
        private Integer amount;

        public static Deduct of(Long userId, Integer amount) {
            return new Deduct(userId, amount);
        }

        private Deduct(Long userId, Integer amount) {
            this.userId = userId;
            this.amount = amount;
        }
    }

    @Getter
    public static class Pay{

        Long userId;
        Integer finalPaymentPrice;

        public static Pay of(Long userId, Integer finalPaymentPrice) {
            return new Pay(userId, finalPaymentPrice);
        }

        private Pay(Long userId, Integer finalPaymentPrice) {
            this.userId = userId;
            this.finalPaymentPrice = finalPaymentPrice;
        }

    }
}
