package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderTestFixture;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserTestFixture;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PaymentTest {

    @Test
    void 전달받은_사용자의_잔고가_부족할경우_isPayable은_false를_리턴(){
        //given
        Payment payment = PaymentTestFixture.createTestPayment();
        User user = payment.getUser();

        //when
        user.deductPoint(user.point());

        // then
        assertThrows(IllegalArgumentException.class, () -> payment.isPayable());
    }


    @Test
    void 주문_생성자와_결제_요청자_정보가_일치하지_않을경우_결제_생성_실패(){
        //given
        User orderUser = UserTestFixture.createTestUserWithIdAndName(1L, "orderUser");
        User paymentUser = UserTestFixture.createTestUserWithIdAndName(2L, "paymentUser");
        Order order = OrderTestFixture.createTestOrderWithUser(orderUser);

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                Payment.createWithOrderValidation(order, paymentUser));

        //then
        assertEquals("사용자 정보가 일치하지않습니다.", exception.getMessage());

    }
}
