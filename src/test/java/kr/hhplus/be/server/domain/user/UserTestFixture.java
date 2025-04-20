package kr.hhplus.be.server.domain.user;

import kr.hhplus.be.server.domain.FakeUser;

public class UserTestFixture {

    public static User createTestUser() {
        return User.createWithName("tester");
    }

    public static User createTestUserWithIdAndName(Long id, String name){
        return new FakeUser(id, name);
    }
}
