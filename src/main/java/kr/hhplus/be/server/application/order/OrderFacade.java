package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.order.command.OrderCreateCommand;
import kr.hhplus.be.server.domain.payment.PaymentCreateCommand;
import kr.hhplus.be.server.domain.payment.PaymentProcessCommand;
import kr.hhplus.be.server.domain.payment.PaymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderFacade {

    private final OrderService orderService;
    private final PaymentService paymentService;

    public OrderFacade(OrderService orderService, PaymentService paymentService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
    }

    @Transactional
    public OrderPaymentResult orderPayment(OrderPaymentCriteria criteria){

        OrderCreateCommand.Response ocResponse =
                orderService.createOrder(OrderCreateCommand.from(criteria));

        PaymentCreateCommand.Response pcResponse =
                paymentService.save(new PaymentCreateCommand(ocResponse.getOrder().getId()));

        PaymentProcessCommand.Response ppResponse =
                paymentService.processPayment(new PaymentProcessCommand(pcResponse.getPayment().getId()));

        return new OrderPaymentResult(ocResponse, ppResponse);
    }
}
