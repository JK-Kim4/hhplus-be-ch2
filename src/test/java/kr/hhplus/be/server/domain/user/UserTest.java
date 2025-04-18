package kr.hhplus.be.server.domain.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserTest {

    String name = "user";
    User user = User.createWithName(name);

    @Test
    void 사용자를_생성한다(){
        assertEquals(name, user.getName());
    }

    @Test
    void 생성된_사용자는_0원의_기본포인트를_갖는다(){
        assertEquals(0, user.point());
    }

    @Test
    void 사용자는_주문목록을_갖는다(){
        User user = User.createWithName(name);

        assertNotNull(user.getOrders());
    }

    @Test
    void 사용자는_포인트를_충전할수있다(){
        //given
        Integer chargeAmount = 100;

        //when
        user.chargePoint(chargeAmount);

        //then
        assertEquals(100, user.point());
    }

    @Test
    void 사용자는_포인트를_차감할수있다(){
        //given
        Integer defaultPoint = 5_000;
        Integer deductPoint = 1_000;
        user = User.createWithName(name);
        user.chargePoint(defaultPoint);

        //when
        user.deductPoint(deductPoint);

        //then
        assertEquals(4_000, user.point());
    }

}
