package kr.hhplus.be.server.integration.orderpayment.application;

import kr.hhplus.be.server.application.order.OrderFacade;
import kr.hhplus.be.server.application.order.criteria.OrderItemCriteria;
import kr.hhplus.be.server.application.order.criteria.OrderPaymentCriteria;
import kr.hhplus.be.server.application.order.result.OrderPaymentResult;
import kr.hhplus.be.server.domain.item.Item;
import kr.hhplus.be.server.domain.item.ItemRepository;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.integration.support.DatabaseCleanup;
import kr.hhplus.be.server.integration.support.InitialTestData;
import kr.hhplus.be.server.integration.support.SampleValues;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Testcontainers
@Transactional
public class OrderFacadeTest {

    @Autowired
    InitialTestData initialTestData;

    @Autowired
    DatabaseCleanup databaseCleanup;

    @Autowired
    OrderFacade orderFacade;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ItemRepository itemRepository;

    SampleValues sampleValues;

    @BeforeEach
    void setUp() {
        databaseCleanup.truncate();
        sampleValues = initialTestData.load();
    }

    @Test
    @Transactional
    void 결제_성공_테스트() {
        //given
        List<OrderItemCriteria> orderItems = Arrays.asList(
                OrderItemCriteria.of(
                        sampleValues.items.car.getId(),
                        sampleValues.items.car.price(),
                        10),
                OrderItemCriteria.of(
                        sampleValues.items.truck.getId(),
                        sampleValues.items.truck.price(),
                        5)
        );
        OrderPaymentCriteria criteria = new OrderPaymentCriteria(sampleValues.user.getId(), null, orderItems);
        Integer carStock = sampleValues.items.car.stock();
        Integer truckStock = sampleValues.items.truck.stock();

        //when
        OrderPaymentResult result = orderFacade.orderPayment(criteria);
        Order order = orderRepository.findById(result.getOrderId()).get();
        Item car = itemRepository.findById(sampleValues.items.car.getId()).get();
        Item truck = itemRepository.findById(sampleValues.items.truck.getId()).get();

        //then
        assertEquals(OrderStatus.PAYMENT_COMPLETED, order.getOrderStatus());
        assertEquals(carStock - 10, car.stock());
        assertEquals(truckStock - 5, truck.stock());
    }


    @Test
    void 주문_결제_진행중_사용자_잔액이_부족할경우_주문생성이_취소된다(){
        //given
        List<OrderItemCriteria> orderItems = Arrays.asList(
                OrderItemCriteria.of(
                        sampleValues.items.car.getId(),
                        sampleValues.items.car.price(),
                        10),
                OrderItemCriteria.of(
                        sampleValues.items.truck.getId(),
                        sampleValues.items.truck.price(),
                        5)
        );
        OrderPaymentCriteria criteria = new OrderPaymentCriteria(sampleValues.user.getId(), null, orderItems);
        User user = userRepository.findById(sampleValues.user.getId()).get();
        user.deductPoint(sampleValues.user.point());

        //when
        IllegalArgumentException illegalArgumentException =
                assertThrows(IllegalArgumentException.class, () -> orderFacade.orderPayment(criteria));
    }

    @Test
    void 주문_결제_진행중_상품재고가_부족할경우_주문생성이_취소된다(){
        List<OrderItemCriteria> orderItems = Arrays.asList(
                OrderItemCriteria.of(
                        sampleValues.items.car.getId(),
                        sampleValues.items.car.price(),
                        sampleValues.items.car.stock() + 1),
                OrderItemCriteria.of(
                        sampleValues.items.truck.getId(),
                        sampleValues.items.truck.price(),
                        sampleValues.items.truck.stock() + 1)
        );
        OrderPaymentCriteria criteria = new OrderPaymentCriteria(sampleValues.user.getId(), null, orderItems);

        assertThrows(IllegalArgumentException.class, ()
                -> orderFacade.orderPayment(criteria));
    }


}
