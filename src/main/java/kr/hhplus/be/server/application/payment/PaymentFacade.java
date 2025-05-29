package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.common.annotation.TrackSales;
import kr.hhplus.be.server.domain.payment.event.PaymentCompletedEvent;
import kr.hhplus.be.server.domain.order.OrderInfo;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.payment.*;
import kr.hhplus.be.server.domain.product.ProductCommand;
import kr.hhplus.be.server.domain.product.ProductInfo;
import kr.hhplus.be.server.domain.product.ProductService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
public class PaymentFacade {

    private final PaymentService paymentService;
    private final OrderService orderService;
    private final ProductService productService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public PaymentFacade(
            PaymentService paymentService,
            OrderService orderService,
            ProductService productService,
            ApplicationEventPublisher applicationEventPublisher) {
        this.paymentService = paymentService;
        this.orderService = orderService;
        this.productService = productService;
        this.applicationEventPublisher = applicationEventPublisher;
    }


    @TrackSales
    public PaymentResult.Pay pay(PaymentCriteria.Pay criteria){
        //TODO validation

        //1. 결제 생성
        PaymentInfo.Create paymentCreate = paymentService.create(PaymentCommand.Create.of(criteria.getOrderId()));

        //2. 주문 상품 조회 / 상품별 DB Lock 획득
        OrderInfo.OrderItems orderItems = orderService.findOrderItemsByOrderId(criteria.getOrderId());
        List<Long> orderItemIds = orderItems.getOrderItems().stream().map(OrderInfo.OrderItem::getProductId).toList();
        ProductInfo.Products products = productService.findByIdInWithPessimisticLock(orderItemIds);

        //3. 사용자 잔고 차감
        paymentService.pay(criteria.toPaymentPayCommand(paymentCreate.getPaymentId()));

        //4. 상품 재고 차감
        productService.deductOrderItemQuantity(ProductCommand.DeductStock.from(orderItems));

        //5. 결과 저장
        PaymentInfo.Complete complete = paymentService.complete(PaymentCommand.Complete.of(criteria.getOrderId(), paymentCreate.getPaymentId()));

        //6. 결제 데이터 플랫폼 전송
        applicationEventPublisher.publishEvent(
            PaymentCompletedEvent.of(
                complete.getPaymentId(),
                complete.getOrderId(),
                complete.getPaidAmount(),
                complete.getPaidAt()
        ));

        return PaymentResult.Pay.from(complete);
    }
}
