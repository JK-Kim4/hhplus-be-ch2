package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.item.ItemService;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.order.command.OrderCommand;
import kr.hhplus.be.server.domain.order.command.OrderInfo;
import kr.hhplus.be.server.domain.payment.command.PaymentCreateCommand;
import kr.hhplus.be.server.domain.payment.command.PaymentInfo;
import kr.hhplus.be.server.domain.payment.command.PaymentProcessCommand;
import kr.hhplus.be.server.domain.payment.PaymentService;
import kr.hhplus.be.server.domain.userCoupon.UserCouponService;
import kr.hhplus.be.server.interfaces.adaptor.RestTemplateAdaptor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class OrderFacade {

    private final OrderService orderService;
    private final PaymentService paymentService;
    private final ItemService itemService;
    private final RestTemplateAdaptor restTemplateAdaptor;
    private final UserCouponService userCouponService;

    public OrderFacade(
            OrderService orderService,
            PaymentService paymentService,
            ItemService itemService,
            RestTemplateAdaptor restTemplateAdaptor, UserCouponService userCouponService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
        this.itemService = itemService;
        this.restTemplateAdaptor = restTemplateAdaptor;
        this.userCouponService = userCouponService;
    }

    @Transactional
    public void orderPayment(OrderPaymentCriteria criteria){

        criteria.getOrderItems().forEach(oi -> {
            itemService.canOrder(oi.toCanOrderCommand());
            itemService.decreaseStock(oi.toDecreaseStockCommand());
        });

        OrderInfo.Create ocResponse = orderService.createOrder(criteria.toCommand());

        if(Objects.nonNull(criteria.getUserCouponId())){
            userCouponService.applyDiscount(ocResponse.getOrderId(), criteria.getUserCouponId());
        }

        PaymentInfo.Create pcResponse =
                paymentService.createPayment(PaymentCreateCommand.of(ocResponse.getOrderId()));

        PaymentInfo.Process ppResponse =
                paymentService.processPayment(new PaymentProcessCommand(pcResponse.getPaymentId()));

        restTemplateAdaptor.post("/external/api-call", ppResponse, ExternalResponse.class);
    }

    public OrderResult.Create createOrder(OrderCommand.Create command) {
        return OrderResult.Create.from(orderService.createOrder(command));
    }
}
