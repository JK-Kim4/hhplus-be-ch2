package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.command.OrderInfo;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.command.PaymentInfo;
import kr.hhplus.be.server.domain.payment.PaymentService;
import kr.hhplus.be.server.interfaces.adaptor.RestTemplateAdaptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class OrderFacaseTest {

    @Mock
    OrderService orderService;
    @Mock
    PaymentService paymentService;
    @Mock
    RestTemplateAdaptor restTemplateAdaptor;

    @InjectMocks
    OrderFacade orderFacade;

    @Test
    @DisplayName("결제 처리가 완료되면 외부 API에 결제 정보를 1회 전송한다(POST)")
    void external_api_call_test(){
        // given
        Order order = mock(Order.class);
        Payment payment = mock(Payment.class);
        List<OrderItemCriteria> orderItems = mock(List.class);

        when(order.getId()).thenReturn(1L);
        when(payment.getId()).thenReturn(1L);

        OrderInfo.Create ocResponse = OrderInfo.Create.from(order);
        PaymentInfo.Create pcResponse = new PaymentInfo.Create(payment.getId());
        PaymentInfo.Process ppResponse = new PaymentInfo.Process(payment);

        when(orderService.createOrder(any())).thenReturn(ocResponse);
        when(paymentService.createPayment(any())).thenReturn(pcResponse);
        when(paymentService.processPayment(any())).thenReturn(ppResponse);

        OrderPaymentCriteria criteria = new OrderPaymentCriteria(1L, null, orderItems);

        // when
        orderFacade.orderPayment(criteria);

        // then
        verify(restTemplateAdaptor, times(1))
                .post(eq("/external/api-call"), eq(ppResponse), eq(ExternalResponse.class));
    }
}
