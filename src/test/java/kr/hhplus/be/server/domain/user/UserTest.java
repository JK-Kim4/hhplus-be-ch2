package kr.hhplus.be.server.domain.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserTest {

    String name = "user";
    User user = new User(name);

    @Test
    void 사용자를_생성한다(){
        Assertions.assertEquals(name, user.getName());
    }

    @Test
    void 생성된_사용자는_0원의_기본포인트를_갖는다(){
        Assertions.assertEquals(0, user.point());
    }

    @Test
    void 사용자는_포인트를_충전할수있다(){
        //given
        Integer chargeAmount = 100;

        //when
        user.chargePoint(chargeAmount);

        //then
        Assertions.assertEquals(100, user.point());
    }

    @Test
    void 사용자는_포인트를_차감할수있다(){
        //given
        Integer defaultPoint = 5_000;
        Integer deductPoint = 1_000;
        user = new User(name);
        user.chargePoint(defaultPoint);

        //when
        user.deductPoint(deductPoint);

        //then
        Assertions.assertEquals(4_000, user.point());
    }

}
