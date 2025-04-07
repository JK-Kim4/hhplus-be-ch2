package kr.hhplus.be.server.domain.user;

public class UserCommand {

    public static class Charge {
        private Long userId;
        private Integer amount;

        public Charge(Long userId, Integer amount) {
            this.userId = userId;
            this.amount = amount;
        }

        public Long getUserId() {
            return userId;
        }

        public Integer getAmount() {
            return amount;
        }
    }

    public static class Find {

        private Long userId;

        public Find(Long userId) {
            this.userId = userId;
        }

        public Long getUserId() {
            return userId;
        }

    }
}
