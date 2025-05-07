package kr.hhplus.be.server.domain.user;

import lombok.Builder;
import lombok.Getter;

import java.util.Optional;

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

    @Getter
    public static class OptionalUser {
        Optional<User> user;

        public static OptionalUser of(Optional<User> user) {
            return OptionalUser.builder().user(user).build();
        }

        @Builder
        private OptionalUser(Optional<User> user) {
            this.user = user;
        }

        public void isPresentThenThrow() {
            if (this.user.isPresent()) {
                throw new IllegalArgumentException("사용할 수 없는 이름입니다.");
            }
        }
    }
}
