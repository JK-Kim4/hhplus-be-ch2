package kr.hhplus.be.server.application.orderPayment;

import kr.hhplus.be.server.application.orderPayment.criteria.OrderItemCriteria;
import kr.hhplus.be.server.application.orderPayment.criteria.OrderPaymentCriteria;
import kr.hhplus.be.server.application.orderPayment.result.OrderResult;
import kr.hhplus.be.server.application.orderPayment.result.PaymentResult;
import kr.hhplus.be.server.domain.item.Item;
import kr.hhplus.be.server.domain.item.ItemService;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentCommand;
import kr.hhplus.be.server.domain.payment.PaymentService;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.domain.userCoupon.UserCoupon;
import kr.hhplus.be.server.domain.userCoupon.UserCouponService;
import kr.hhplus.be.server.interfaces.adaptor.RestTemplateAdaptor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderFacade {

    private final OrderService orderService;
    private final PaymentService paymentService;
    private final ItemService itemService;
    private final UserCouponService userCouponService;
    private final UserService userService;
    private final RestTemplateAdaptor restTemplateAdaptor;

    public OrderFacade(
            OrderService orderService,
            PaymentService paymentService,
            ItemService itemService,
            RestTemplateAdaptor restTemplateAdaptor,
            UserCouponService userCouponService,
            UserService userService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
        this.itemService = itemService;
        this.restTemplateAdaptor = restTemplateAdaptor;
        this.userCouponService = userCouponService;
        this.userService = userService;
    }


    @Transactional
    public void orderPayment(OrderPaymentCriteria criteria){
        processPayment(
                new OrderPaymentCriteria.PaymentProcess(
                        createPayment(new OrderPaymentCriteria.PaymentCreate(
                                createOrder(criteria).getOrderId())).getPaymentId()));
    }

    @Transactional
    public OrderResult.Create createOrder(OrderPaymentCriteria criteria) {
        for (OrderItemCriteria orderItem : criteria.getOrderItems()) {
            Item item = itemService.findItemById(orderItem.getItemId());
            item.hasEnoughStock(orderItem.getQuantity());
        }

        //주문 객체 생성(User)
        User user = userService.findById(criteria.getUserId());
        Order order = new Order(user);

        //주문 상품 객체 생성(OrderItem)
        List<OrderItem> orderItems = orderService.createOrderItems(order, criteria.toOrderItemCreateCommand());
        order.addOderItems(orderItems);
        order.calculateTotalPrice();

        //쿠폰 적용
        if(criteria.getUserCouponId() != null){
            UserCoupon userCoupon = userCouponService.findUserCouponById(criteria.getUserCouponId());
            userCoupon.isUsable(LocalDateTime.now(), user.getId());
            order.applyCoupon(userCoupon);
        }

        //DB 저장
        orderService.save(order);
        return OrderResult.Create.from(order);
    }

    @Transactional
    public PaymentResult.Create createPayment(OrderPaymentCriteria.PaymentCreate criteria) {
        Order order = orderService.findById(criteria.getOrderId());
        User user = userService.findById(order.getUserId());

        Payment payment = new Payment(order, user);
        order.registerPayment(payment);

        paymentService.save(PaymentCommand.Create.of(payment));

        return PaymentResult.Create.from(payment);
    }

    @Transactional
    public PaymentResult.Process processPayment(OrderPaymentCriteria.PaymentProcess criteria) {
        Payment payment = paymentService.findById(criteria.getPaymentId());

        payment.pay();

        //외부 API Call
        restTemplateAdaptor.post("/external/api-call", payment, ExternalResponse.class);

        return PaymentResult.Process.from(payment);
    }
}
