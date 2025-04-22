package kr.hhplus.be.server.domain.user;

public class UserCommand {

    public static class Create{

        private String name;

        public Create(String name){
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

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

    public static class Deduct {

        private Long userId;
        private Integer amount;

        public Deduct(Long userId, Integer amount) {
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
}
