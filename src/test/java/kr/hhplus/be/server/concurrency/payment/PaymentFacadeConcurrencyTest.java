package kr.hhplus.be.server.concurrency.payment;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.application.payment.PaymentCriteria;
import kr.hhplus.be.server.application.payment.PaymentFacade;
import kr.hhplus.be.server.domain.item.Item;
import kr.hhplus.be.server.domain.item.ItemRepository;
import kr.hhplus.be.server.domain.item.ItemService;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Sql(scripts = {"/import_test_user.sql", "/import_test_order.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD )
@Testcontainers
public class PaymentFacadeConcurrencyTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PaymentFacade paymentFacade;

    @Autowired
    ItemService itemService;
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository ordersRepository;


    @BeforeEach
    void beforeEach() {


    }

    @Test
    @Transactional
    void 상품_재고_차감_동시성_테스트() throws InterruptedException {
        List<Order> orderList = ordersRepository.findAll();
        for(Order order : orderList) {
            System.out.println(order.getId());
        }
        List<Throwable> exceptions = Collections.synchronizedList(new ArrayList<>());
        CountDownLatch latch = new CountDownLatch(orderList.size());
        ExecutorService executor = Executors.newFixedThreadPool(10);

        for(Order order : orderList) {
            PaymentCriteria.Create create = PaymentCriteria.Create.of(order.getOrderUserId(), order.getId());
            executor.execute(() -> {
                try {
                    paymentFacade.payment(create);
                }catch (Exception e) {
                    exceptions.add(e);
                    e.printStackTrace();
                }finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        List<Order> updatedOrders = ordersRepository.findAll();
        for(Order order : updatedOrders) {
            System.out.println("order id : " + order.getId() + ", order status : " + order.getOrderStatus());
        }
        Item item = itemRepository.findById(1L).orElseThrow();
        assertEquals(0, item.stock());
    }


}
