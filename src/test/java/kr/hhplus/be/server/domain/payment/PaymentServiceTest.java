package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.order.OrderTestFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void 결제_완료_일시와_주문_상태를_수정한다(){
        //given
        Order order = OrderTestFixture.createTestOrder();
        Payment payment = PaymentTestFixture.creatTestPaymentWithOrderAndUser(order);

        //when
        paymentService.success(payment, order);

        //then
        assertEquals(OrderStatus.PAYMENT_COMPLETED, order.getOrderStatus());
    }

}
