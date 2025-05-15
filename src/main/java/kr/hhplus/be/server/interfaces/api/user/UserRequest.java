package kr.hhplus.be.server.interfaces.api.user;

import kr.hhplus.be.server.application.user.UserCriteria;
import lombok.Builder;
import lombok.Getter;

public class UserRequest {

    @Getter
    public static class Create {
        String username;


        public static Create of(String username) {
            return new Create(username);
        }

        @Builder
        private Create(String username) {
            this.username = username;
        }

        public UserCriteria.Create toCriteria() {
            return UserCriteria.Create.builder().username(username).build();
        }
    }

    public class Coupons {
    }
}
