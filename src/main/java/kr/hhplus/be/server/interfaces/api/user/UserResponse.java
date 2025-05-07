package kr.hhplus.be.server.interfaces.api.user;

import kr.hhplus.be.server.application.user.UserResult;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

public class UserResponse {

    @Getter
    public static class Create {

        Long userId;
        String username;
        BigDecimal point;

        public static Create from(UserResult.Create user) {
            return new Create(user.getUserId(), user.getUsername(), user.getPoint());
        }

        @Builder
        private Create(Long userId, String username, BigDecimal point) {
            this.userId = userId;
            this.username = username;
            this.point = point;
        }
    }
}
