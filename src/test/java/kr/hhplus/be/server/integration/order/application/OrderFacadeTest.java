package kr.hhplus.be.server.integration.order.application;

import kr.hhplus.be.server.application.coupon.CouponFacade;
import kr.hhplus.be.server.application.orderPayment.OrderFacade;
import kr.hhplus.be.server.application.orderPayment.criteria.OrderItemCriteria;
import kr.hhplus.be.server.application.orderPayment.criteria.OrderPaymentCriteria;
import kr.hhplus.be.server.application.orderPayment.result.OrderResult;
import kr.hhplus.be.server.domain.couponv2.*;
import kr.hhplus.be.server.domain.item.Item;
import kr.hhplus.be.server.domain.item.ItemRepository;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.domain.user.userCoupon.UserCouponCriteria;
import kr.hhplus.be.server.domain.user.userCoupon.UserCouponInfo;
import kr.hhplus.be.server.interfaces.exception.InvalidPriceException;
import kr.hhplus.be.server.interfaces.exception.InvalidStockException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@Transactional
public class OrderFacadeTest {

    @Autowired OrderFacade orderFacade;
    @Autowired OrderRepository orderRepository;
    @Autowired UserRepository userRepository;
    @Autowired ItemRepository itemRepository;
    @Autowired CouponV2Service couponV2Service;
    @Autowired CouponFacade couponFacade;
    @Autowired CouponV2Repository couponV2Repository;

    User user;

    Item item1;
    Item item2;
    Item item3;

    CouponV2 coupon;
    Long userCouponId;

    List<OrderItemCriteria> orderItemCriteria;

    @BeforeEach
    void setUp() {
        user = new User("Tester");
        userRepository.save(user);

        item1 = new Item("car", 50_000, 10);
        item2 = new Item("truck", 10_000, 50);
        item3 = new Item("food", 3_000, 5);
        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);

        coupon = new CouponV2("test", CouponType.FLAT, 10,
                LocalDate.of(2025, 12, 31), LocalDate.of(2025,1,1));
        new FlatDiscountCouponV2(coupon, 50_000);
        couponV2Repository.save(coupon);

        UserCouponInfo.Issue issue = couponFacade.issue(new UserCouponCriteria.Issue(coupon.getId(), user.getId()));
        userCouponId = issue.getUserCouponId();

        orderItemCriteria =
                Arrays.asList(
                        OrderItemCriteria.of(item1.getId(), 50_000, 5),
                        OrderItemCriteria.of(item2.getId(), 10_000, 5),
                        OrderItemCriteria.of(item3.getId(), 3_000, 5)
                );
    }

    @Nested
    @DisplayName("신규 주문 생성 테스트")
    class create_order_test{
        @Test
        void 주문생성_성공시_주문과_주문아이템정보를_저장한다(){
            //given
            OrderPaymentCriteria criteria = new OrderPaymentCriteria(user.getId(), null, orderItemCriteria);
            OrderResult.Create result = orderFacade.createOrder(criteria);

            //when
            Order savedOrder = orderRepository.findById(result.getOrderId()).get();
            List<OrderItem> orderItems = orderRepository.findOrderItemsByOrderId(result.getOrderId());

            assertEquals(OrderStatus.ORDER_CREATED, savedOrder.getOrderStatus());
            assertEquals((50_000 * 5 + 10_000 * 5 + 3_000 * 5), savedOrder.getTotalPrice());
            assertEquals(3, orderItems.size());
        }

        @Test
        void 유효한_쿠폰정보_포함시_할인이_적용된_주문을_생성하고_저장한다(){
            //given
            OrderPaymentCriteria criteria = new OrderPaymentCriteria(user.getId(), userCouponId, orderItemCriteria);
            OrderResult.Create result = orderFacade.createOrder(criteria);

            //when
            Order savedOrder = orderRepository.findById(result.getOrderId()).get();
            UserCouponV2 userCoupon = couponV2Service.findUserCouponById(userCouponId);

            Integer discountResult = coupon.calculateDiscount(savedOrder.getTotalPrice());

            assertEquals(OrderStatus.ORDER_CREATED, savedOrder.getOrderStatus());
            assertEquals(savedOrder, userCoupon.getAppliedOrder());
            assertEquals(savedOrder.getFinalPaymentPrice(), discountResult);
        }

        @Test
        void 주문상품_가격정보가_올바르지않을경우_주문생성이_취소된다(){
            //given
            orderItemCriteria =
                    Arrays.asList(
                            OrderItemCriteria.of(item1.getId(), 1234, item1.stock()),
                            OrderItemCriteria.of(item2.getId(), 12345, item2.stock()),
                            OrderItemCriteria.of(item3.getId(), 1235, item3.stock())
                    );
            OrderPaymentCriteria criteria = new OrderPaymentCriteria(user.getId(), null, orderItemCriteria);

            //when
            assertThrows(InvalidPriceException.class,
                    () -> orderFacade.createOrder(criteria));
        }

        @Test
        void 주문상품_재고가_부족할경우_주문생성이_취소된다(){
            //given
            orderItemCriteria =
                    Arrays.asList(
                            OrderItemCriteria.of(item1.getId(), item1.price(), 1000),
                            OrderItemCriteria.of(item2.getId(), item2.price(), 6000),
                            OrderItemCriteria.of(item3.getId(), item3.price(), 1000)
                    );
            OrderPaymentCriteria criteria = new OrderPaymentCriteria(user.getId(), null, orderItemCriteria);

            //when
            assertThrows(InvalidStockException.class,
                    () -> orderFacade.createOrder(criteria));
        }
    }

    @Nested
    @DisplayName("결제 생성 테스트")
    class payment_create_test {

    }



}
