package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.command.OrderCreateCommand;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentProcessCommand;

public class OrderPaymentResult {

    private Order order;
    private Payment payment;

    public OrderPaymentResult(
            OrderCreateCommand.Response ocResponse,
            PaymentProcessCommand.Response ppResponse) {
        this.order = ocResponse.getOrder();
        this.payment = ppResponse.getPayment();
    }

    public Payment getPayment() {
        return payment;
    }

    public Order getOrder() {
        return order;
    }
}
