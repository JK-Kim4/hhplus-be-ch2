package kr.hhplus.be.server.domain.payment;

import jakarta.persistence.NoResultException;
import kr.hhplus.be.server.domain.balance.Balance;
import kr.hhplus.be.server.domain.balance.BalanceRepository;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderItems;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final BalanceRepository balanceRepository;
    private final ProductRepository productRepository;

    public PaymentService(
            PaymentRepository paymentRepository,
            OrderRepository orderRepository,
            BalanceRepository balanceRepository,
            ProductRepository productRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.balanceRepository = balanceRepository;
        this.productRepository = productRepository;
    }

    public PaymentInfo.Pay pay(PaymentCommand.Pay command){
        Order order = orderRepository.findById(command.getOrderId())
                .orElseThrow(NoResultException::new);
        Payment payment = Payment.create(order);

        //1.주문/상품 조회 (DB Lock)
        List<Long> orderItemIds = order.getOrderItems().stream().map(OrderItem::getProductId).toList();
        List<Product> products = productRepository.findByIdInWithPessimisticLock(orderItemIds);
        OrderItems orderItems = new OrderItems(orderRepository.findOrderItemsByOrderId(command.getOrderId()));

        //2. 사용자 잔고 차감
        Balance balance = balanceRepository.findByUserId(command.userId)
                .orElseThrow(NoResultException::new);
        payment.pay(balance);

        //3. 상품 재고 차감
        // OrderItem 목록을 productId → quantity 맵으로 변환
        Map<Long, Integer> quantityMap = orderItems.getItems().stream()
                .collect(Collectors.toMap(OrderItem::getProductId, OrderItem::getQuantity));

        for (Product product : products) {
            int quantity = quantityMap.getOrDefault(product.getId(), 0);
            product.decreaseStock(quantity);
        }

        //4. 결제 완료 처리
        payment.complete(order);
        paymentRepository.save(payment);

        return PaymentInfo.Pay.from(payment);
    }
}
