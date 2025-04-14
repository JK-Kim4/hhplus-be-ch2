package kr.hhplus.be.server.domain.user;

public class UserInfo {

    public static class Create{

        private Long userId;
        private String name;

        public static Create from(kr.hhplus.be.server.domain.user.User user){
            return new Create(user);
        }

        private Create(kr.hhplus.be.server.domain.user.User user){
            this.userId = user.getId();
            this.name = user.getName();
        }

        public Long getUserId() {
            return userId;
        }

        public String getName() {
            return name;
        }
    }

    public static class User {

        private Long userId;
        private String name;

        public static User from (kr.hhplus.be.server.domain.user.User user){
            return new User(user);
        }

        private User(kr.hhplus.be.server.domain.user.User user){
            this.userId = user.getId();
            this.name = user.getName();
        }

        public Long getUserId() {
            return userId;
        }

        public String getName() {
            return name;
        }
    }
}
