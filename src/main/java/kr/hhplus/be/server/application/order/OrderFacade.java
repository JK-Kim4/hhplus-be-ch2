package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.application.coupon.CouponCommandService;
import kr.hhplus.be.server.application.coupon.CouponQueryService;
import kr.hhplus.be.server.application.user.UserCommandService;
import kr.hhplus.be.server.application.user.UserQueryService;
import kr.hhplus.be.server.domain.item.ItemService;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentService;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserCommand;
import kr.hhplus.be.server.interfaces.common.client.RestTemplateClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component
@Transactional
public class OrderFacade {

    private final OrderService orderService;
    private final PaymentService paymentService;
    private final ItemService itemService;

    private final CouponCommandService couponCommandService;
    private final CouponQueryService couponQueryService;

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    private final RestTemplateClient restTemplateClient;

    public OrderFacade(
            OrderService orderService,
            PaymentService paymentService,
            ItemService itemService,
            CouponCommandService couponCommandService,
            CouponQueryService couponQueryService,
            UserCommandService userCommandService,
            UserQueryService userQueryService,
            RestTemplateClient restTemplateClient) {
        this.orderService = orderService;
        this.paymentService = paymentService;
        this.itemService = itemService;
        this.couponCommandService = couponCommandService;
        this.couponQueryService = couponQueryService;
        this.userCommandService = userCommandService;
        this.userQueryService = userQueryService;
        this.restTemplateClient = restTemplateClient;
    }


    @Transactional
    public OrderPaymentResult orderPayment(OrderPaymentCriteria criteria){
        //주문 생성
        User user = userQueryService.findById(criteria.getUserId());
        List<OrderItem> orderItems = itemService.getOrderItems(criteria.toOrderItemCreateCommand());
        Order order = orderService.create(user, orderItems);

        //쿠폰 적용
        Optional.ofNullable(criteria.getUserCouponId())
                .map(couponQueryService::findUserCouponById)
                .map((uc) -> couponCommandService.applyCouponToOrder(uc, order));

        //결제 생성
        Payment payment = paymentService.create(order, user);

        //결제 진행(잔액 감소)
        userCommandService.deduct(UserCommand.Deduct.of(user.getId(), payment.getPaymentPrice()));

        //결제 진행(재고 차감)
        orderItems.forEach(OrderItem::deductItemQuantity);

        //결과 반영
        paymentService.success(payment, order);

        restTemplateClient.post("test", payment, HashMap.class);

        return new OrderPaymentResult(
                order.getId(),
                payment.getId(),
                payment.getPaymentPrice()
        );
    }
}
