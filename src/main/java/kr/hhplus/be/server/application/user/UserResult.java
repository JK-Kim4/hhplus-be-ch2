package kr.hhplus.be.server.application.user;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

public class UserResult {

    @Getter
    public static class Create {
        Long userId;
        String username;
        BigDecimal point;

        public static Create of(Long userId, String username, BigDecimal point){
            return Create.builder().userId(userId).username(username).point(point).build();
        }

        @Builder
        private Create(Long userId, String username, BigDecimal point) {
            this.userId = userId;
            this.username = username;
            this.point = point;
        }
    }
}
