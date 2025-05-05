package kr.hhplus.be.server.application.payment;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.application.user.UserCommandService;
import kr.hhplus.be.server.application.user.UserQueryService;
import kr.hhplus.be.server.domain.item.ItemCommand;
import kr.hhplus.be.server.domain.item.ItemService;
import kr.hhplus.be.server.domain.order.OrderCommand;
import kr.hhplus.be.server.domain.order.OrderInfo;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.payment.PaymentCommand;
import kr.hhplus.be.server.domain.payment.PaymentInfo;
import kr.hhplus.be.server.domain.payment.PaymentService;
import kr.hhplus.be.server.domain.user.UserCommand;
import kr.hhplus.be.server.interfaces.common.client.RestTemplateClient;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@Transactional
public class PaymentFacade {

    private final PaymentService paymentService;
    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;
    private final OrderService orderService;
    private final ItemService itemService;
    private final RestTemplateClient restTemplateClient;

    public PaymentFacade(
            PaymentService paymentService,
            UserQueryService userQueryService,
            UserCommandService userCommandService,
            OrderService orderService,
            ItemService itemService,
            RestTemplateClient restTemplateClient) {
        this.paymentService = paymentService;
        this.userQueryService = userQueryService;
        this.userCommandService = userCommandService;
        this.orderService = orderService;
        this.itemService = itemService;
        this.restTemplateClient = restTemplateClient;
    }

    public PaymentResult.Payment payment(PaymentCriteria.Create criteria){
        //주문 조회
        OrderInfo.Payment order = orderService.getOrderForPayment(criteria.getOrderId());

        //사용자 검증 / 결제 가능 여부 검증
        userQueryService.isPayable(criteria.userId, order.getFinalPrice());

        //잔액 차감
        userCommandService.pay(UserCommand.Pay.of(criteria.userId, order.getFinalPrice()));

        //재고 차감
        order.getOrderItems().forEach((orderItem)
                -> itemService.deductStock(ItemCommand.Deduct.of(orderItem.getItemId(), orderItem.getQuantity())));

        //결제 생성
        PaymentInfo.Create payment = paymentService.create(PaymentCommand.Create.of(criteria.orderId, criteria.userId));

        //결제 후처리
        orderService.registerPayment(OrderCommand.RegisterPayment.of(order.getOrderId(), payment.getPaymentId()));
        restTemplateClient.post("test", payment, HashMap.class);

        return PaymentResult.Payment.from(payment);
    }
}
