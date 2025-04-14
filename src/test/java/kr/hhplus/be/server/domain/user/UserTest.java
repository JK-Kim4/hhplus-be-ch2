package kr.hhplus.be.server.domain.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserTest {

    @Test
    void user_create() {
        String username = "test";
        User user = new User(username);

        assertNotNull(user);
    }
}
