package kr.hhplus.be.server.integration.order;

import kr.hhplus.be.server.application.coupon.CouponCommand;
import kr.hhplus.be.server.application.coupon.CouponCommandService;
import kr.hhplus.be.server.application.order.OrderCriteria;
import kr.hhplus.be.server.application.order.OrderFacade;
import kr.hhplus.be.server.application.order.OrderResult;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.CouponTestFixture;
import kr.hhplus.be.server.domain.item.ItemRepository;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.payment.PaymentRepository;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.integration.support.DatabaseCleanup;
import kr.hhplus.be.server.integration.support.InitialTestData;
import kr.hhplus.be.server.integration.support.SampleValues;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Testcontainers
public class OrderFacadeTest {

    @Autowired
    OrderFacade orderFacade;

    @Autowired public CouponRepository couponRepository;
    @Autowired public ItemRepository itemRepository;
    @Autowired public OrderRepository orderRepository;
    @Autowired public PaymentRepository paymentRepository;
    @Autowired public UserRepository userRepository;
    @Autowired public InitialTestData initialTestData;
    @Autowired public DatabaseCleanup databaseCleanup;

    @Autowired
    CouponCommandService couponCommandService;

    SampleValues sampleValues;

    @BeforeEach
    void setUp() {
        databaseCleanup.truncate();
        sampleValues = initialTestData.load();

        userRepository.save(sampleValues.user);
        itemRepository.save(sampleValues.items.car);
        itemRepository.save(sampleValues.items.truck);
        itemRepository.save(sampleValues.items.book);
    }

    @Test
    @DisplayName("주문 정보를 생성한다")
    void create_order_test(){
        //given
        List<OrderCriteria.Item> orderItems = List.of(
                OrderCriteria.Item.of(sampleValues.items.car.getId(), sampleValues.items.car.price(), 10)
        );
        OrderCriteria.Create criteria = OrderCriteria.Create.of(sampleValues.user.getId(), null, orderItems);
        Integer totalPrice = sampleValues.items.car.price() * 10;

        //when
        OrderResult.Order order = orderFacade.order(criteria);

        //then
        assertNotNull(order);
        assertEquals(totalPrice, order.getTotalPrice());
    }

    @Test
    @DisplayName("쿠폰이 적용된 주문 생성시 할인이 적용된 최종 금액이 계산된다.")
    void user_coupon_apply_order_create(){
        //given
        Coupon 할인_쿠폰_5000원 = CouponTestFixture.createTestCouponWithDiscountPrice(5000);
        couponRepository.save(할인_쿠폰_5000원);

        Long userCouponId = couponCommandService.issue(CouponCommand.Issue.of(할인_쿠폰_5000원.getId(), sampleValues.user.getId())).getUserCouponId();
        List<OrderCriteria.Item> orderItems = List.of(
                OrderCriteria.Item.of(sampleValues.items.car.getId(), sampleValues.items.car.price(), 10)
        );
        OrderCriteria.Create criteria = OrderCriteria.Create.of(sampleValues.user.getId(), userCouponId, orderItems);

        //when
        OrderResult.Order order1 = orderFacade.order(criteria);
        Order order = orderRepository.findById(order1.getOrderId()).get();

        //then
        assertNotNull(order);
        assertEquals(sampleValues.items.car.price() * 10 - 5000, order.getFinalPaymentPrice());
        assertEquals(userCouponId, order.getUserCouponId());
    }
}
