package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.item.Item;
import kr.hhplus.be.server.domain.item.ItemService;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.order.command.OrderCommand;
import kr.hhplus.be.server.domain.payment.Payment;
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
        //상품 주문 가능 여부 확인
        for (OrderItemCriteria orderItem : criteria.getOrderItems()) {
            Item item = itemService.findItemById(orderItem.getItemId());
            item.hasEnoughStock(orderItem.getQuantity());
        }

        //주문 객체 생성(User)
        User user = userService.findUserById(criteria.getUserId());
        Order order = new Order(user);

        //주문 상품 객체 생성(OrderItem)
        List<OrderItem> orderItems = orderService.createOrderItems(order, criteria.getOrderItemCreateCommand());
        order.addOderItems(orderItems);
        order.calculateTotalPrice();

        //쿠폰 적용
        if(criteria.getUserCouponId() != null){
            UserCoupon userCoupon = userCouponService.findUserCouponById(criteria.getUserCouponId());
            userCoupon.isUsable(LocalDateTime.now());
            order.applyCoupon(userCoupon);
        }

        //결제 생성
        Payment payment = new Payment(order, user);


        //결제 처리
        order.deductOrderItemStock();
        payment.pay();


        //결과 저장
        orderService.save(order);
        paymentService.save(payment);


        //외부 API Call
        restTemplateAdaptor.post("/external/api-call", payment, ExternalResponse.class);
    }

    public OrderResult.Create createOrder(OrderCommand.Create command) {
        return OrderResult.Create.from(orderService.createOrder(command));
    }
}
