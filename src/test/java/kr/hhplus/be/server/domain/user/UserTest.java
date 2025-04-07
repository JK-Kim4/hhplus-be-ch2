package kr.hhplus.be.server.domain.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserTest {

    @Test
    void 보유_포인트가_0원인_기본_사용자를_생성한다(){
        //given
        String name = "test";
        Long id = 1L;
        Integer defaultPoint = 0;

        //when
        User user = new User(id, name);

        //then
        Assertions.assertEquals(defaultPoint, user.point());
    }

    @Test
    void 초기_포인트를_보유한_사용자를_생성한다(){
        //given
        String name = "test";
        Long id = 1L;
        Integer initPoint = 1000;

        //when
        User user = new User(id, name, initPoint);

        //then
        Assertions.assertEquals(initPoint, user.point());
    }

    @Test
    void 사용자의_포인트를_충전한다(){
        //given
        String name = "test";
        Long id = 1L;
        Integer chargePoint = 1000;

        //when
        User user = new User(id, name);
        user.chargePoint(chargePoint);

        //then
        Assertions.assertEquals(chargePoint, user.point());
    }

    @Test
    void 사용자_포인트를_차감한다(){
        //given
        String name = "test";
        Long id = 1L;
        Integer defaultPoint = 1000;
        Integer deductPoint = 900;

        //when
        User user = new User(id, name, defaultPoint);
        user.deductPoint(deductPoint);

        //then
        Assertions.assertEquals(defaultPoint - deductPoint, user.point());
    }


}
