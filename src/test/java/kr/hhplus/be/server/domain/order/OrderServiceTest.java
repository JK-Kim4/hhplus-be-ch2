package kr.hhplus.be.server.domain.order;

import jakarta.persistence.NoResultException;
import kr.hhplus.be.server.domain.payment.Payment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void 고유번호를_전달받아_주문의_상세정보를_조회할때_존재하지않으면_NoResultException(){
        //given
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        //when//then
        assertThrows(NoResultException.class, ()
                -> orderService.findById(1L));
    }

    @Test
    void 주문_결제정보를_전달받아_결제를_처리한다(){
        //given
        Order order = OrderTestFixture.createTestOrder();
        Payment payment = new Payment(order);

        //when
        orderService.processPayment(order,payment);

        //then
        assertEquals(OrderStatus.PAYMENT_COMPLETED, order.getOrderStatus());
    }

}
