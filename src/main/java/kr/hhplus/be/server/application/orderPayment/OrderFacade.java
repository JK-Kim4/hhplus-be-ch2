package kr.hhplus.be.server.application.orderPayment;

import kr.hhplus.be.server.application.orderPayment.criteria.OrderPaymentCriteria;
import kr.hhplus.be.server.application.orderPayment.result.OrderPaymentResult;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.item.ItemService;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentService;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserService;
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
    private final CouponService couponService;
    private final UserService userService;
    private final RestTemplateClient restTemplateClient;

    public OrderFacade(
            OrderService orderService,
            PaymentService paymentService,
            ItemService itemService,
            CouponService couponService,
            UserService userService,
            RestTemplateClient restTemplateClient) {
        this.orderService = orderService;
        this.paymentService = paymentService;
        this.itemService = itemService;
        this.couponService = couponService;
        this.userService = userService;
        this.restTemplateClient = restTemplateClient;
    }


    @Transactional
    public OrderPaymentResult orderPayment(OrderPaymentCriteria criteria){
        //주문 생성
        User user = userService.findById(criteria.getUserId());
        List<OrderItem> orderItems = itemService.getOrderItems(criteria.toOrderItemCreateCommand());
        Order order = orderService.create(user, orderItems);

        //쿠폰 적용
        Optional.ofNullable(criteria.getUserCouponId())
                .map(couponService::findUserCouponById)
                .map((uc) -> couponService.applyCouponToOrder(uc, order));

        //결제 생성
        Payment payment = paymentService.create(order);

        //결제 진행
        paymentService.processPayment(payment, order);

        //결과 저장
        orderService.save(order);
        paymentService.save(payment);

        restTemplateClient.post("test", payment, HashMap.class);

        return new OrderPaymentResult(
                order.getId(),
                payment.getId(),
                payment.getPaymentPrice()
        );
    }
}
