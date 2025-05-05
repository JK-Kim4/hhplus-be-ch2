package kr.hhplus.be.server.domain.user;

import lombok.Builder;
import lombok.Getter;

public class UserInfo {

    @Getter
    public static class Create {
        Long userId;
        String name;

        public static Create from(User user) {
            return Create.builder()
                    .userId(user.getId())
                    .name(user.getName())
                    .build();
        }

        @Builder
        public Create(Long userId, String name) {
            this.userId = userId;
            this.name = name;
        }
    }
}
