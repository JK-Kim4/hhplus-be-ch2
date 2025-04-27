package kr.hhplus.be.server.domain.user;

import lombok.Getter;

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

        public static Charge of(Long userId, Integer amount) {
            return new Charge(userId, amount);
        }

        private Charge(Long userId, Integer amount) {
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
}
