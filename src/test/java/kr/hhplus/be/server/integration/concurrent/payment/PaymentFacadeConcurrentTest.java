package kr.hhplus.be.server.integration.concurrent.payment;

import kr.hhplus.be.server.application.payment.PaymentCriteria;
import kr.hhplus.be.server.application.payment.PaymentFacade;
import kr.hhplus.be.server.support.ConcurrentTestExecutor;
import kr.hhplus.be.server.domain.balance.Balance;
import kr.hhplus.be.server.domain.balance.BalanceRepository;
import kr.hhplus.be.server.domain.balance.Point;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderRepository;
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
    void setup() {
        User user1 = createUserWithBalance("tester", BigDecimal.valueOf(100_000));
        User user2 = createUserWithBalance("tester", BigDecimal.valueOf(100_000));
        User user3 = createUserWithBalance("tester", BigDecimal.valueOf(100_000));
        User user4 = createUserWithBalance("tester", BigDecimal.valueOf(100_000));

        Product product1 = createProduct("product1", BigDecimal.valueOf(10_000), 2);
        productId = product1.getId();

        Order order1 = createOrder(user1.getId(), product1);
        Order order2 = createOrder(user2.getId(), product1);
        Order order3 = createOrder(user3.getId(), product1);
        Order order4 = createOrder(user4.getId(), product1);

        pay1 = toPaymentCriteria(user1, order1);
        pay2 = toPaymentCriteria(user2, order2);
        pay3 = toPaymentCriteria(user3, order3);
        pay4 = toPaymentCriteria(user4, order4);
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


    private User createUserWithBalance(String name, BigDecimal balanceAmount) {
        User user = User.createWithName(name);
        userRepository.save(user);
        balanceRepository.save(Balance.create(user.getId(), Point.of(balanceAmount)));
        return user;
    }

    private Product createProduct(String name, BigDecimal price, int stock) {
        return productRepository.save(Product.create(name, price, stock));
    }

    private Order createOrder(Long userId, Product product) {
        Order order = Order.create(userId, List.of(OrderItem.create(product.getId(), product.getPrice(), 1)));
        return orderRepository.save(order);
    }

    private PaymentCriteria.Pay toPaymentCriteria(User user, Order order) {
        return PaymentCriteria.Pay.builder()
                .userId(user.getId())
                .orderId(order.getId())
                .build();
    }
}
