package kr.hhplus.be.server.concurrent.payment;

import kr.hhplus.be.server.application.payment.PaymentCriteria;
import kr.hhplus.be.server.application.payment.PaymentFacade;
import kr.hhplus.be.server.concurrent.support.ConcurrentTestExecutor;
import kr.hhplus.be.server.domain.balance.Balance;
import kr.hhplus.be.server.domain.balance.BalanceRepository;
import kr.hhplus.be.server.domain.balance.Point;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
@Testcontainers
public class PaymentFacadeConcurrentTest {

    @Autowired
    PaymentFacade paymentFacade;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    BalanceRepository balanceRepository;

    PaymentCriteria.Pay pay1;
    PaymentCriteria.Pay pay2;
    PaymentCriteria.Pay pay3;
    PaymentCriteria.Pay pay4;

    Long productId;

    @BeforeEach
    void setup(){
        User user1 = User.createWithName("tester");
        userRepository.save(user1);
        User user2 = User.createWithName("tester");
        userRepository.save(user2);
        User user3 = User.createWithName("tester");
        userRepository.save(user3);
        User user4 = User.createWithName("tester");
        userRepository.save(user4);
        balanceRepository.save(Balance.create(user1.getId(), Point.of(BigDecimal.valueOf(100_000))));
        balanceRepository.save(Balance.create(user2.getId(), Point.of(BigDecimal.valueOf(100_000))));
        balanceRepository.save(Balance.create(user3.getId(), Point.of(BigDecimal.valueOf(100_000))));
        balanceRepository.save(Balance.create(user4.getId(), Point.of(BigDecimal.valueOf(100_000))));

        Product product1 = productRepository.save(Product.create("product1", BigDecimal.valueOf(10_000), 2));
        productId = product1.getId();

        Order order1 = Order.create(user1.getId(), List.of(OrderItem.create(product1.getId(), product1.getPrice(), 1)));
        Order order2 = Order.create(user2.getId(), List.of(OrderItem.create(product1.getId(), product1.getPrice(), 1)));
        Order order3 = Order.create(user3.getId(), List.of(OrderItem.create(product1.getId(), product1.getPrice(), 1)));
        Order order4 = Order.create(user4.getId(), List.of(OrderItem.create(product1.getId(), product1.getPrice(), 1)));
        orderRepository.save(order1);
        orderRepository.save(order2);
        orderRepository.save(order3);
        orderRepository.save(order4);

        pay1 = PaymentCriteria.Pay.builder().userId(user1.getId()).orderId(order1.getId()).build();
        pay2 = PaymentCriteria.Pay.builder().userId(user2.getId()).orderId(order2.getId()).build();
        pay3 = PaymentCriteria.Pay.builder().userId(user3.getId()).orderId(order3.getId()).build();
        pay4 = PaymentCriteria.Pay.builder().userId(user4.getId()).orderId(order4.getId()).build();

    }

    @Test
    void 결제_상품재고_동시차감_테스트() throws InterruptedException {
        List<Runnable> tasks = List.of(
                () -> paymentFacade.pay(pay1),
                () -> paymentFacade.pay(pay2),
                () -> paymentFacade.pay(pay3),
                () -> paymentFacade.pay(pay4)
        );


        List<Throwable> execute = ConcurrentTestExecutor.execute(5, tasks);

        productRepository.flush();
        Product product = productRepository.findById(productId).get();

        Assertions.assertEquals(0, product.getQuantity());
        Assertions.assertEquals(2, execute.size());



    }
}
