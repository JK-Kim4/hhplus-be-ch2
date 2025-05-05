package kr.hhplus.be.server.domain.user;

import lombok.Builder;
import lombok.Getter;

public class UserCommand {


    @Getter
    public static class Create {
        String name;

        @Builder
        public Create(String name) {
            this.name = name;
        }

    }
}
