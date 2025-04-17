package kr.hhplus.be.server.integration.orderpayment.application;

import kr.hhplus.be.server.application.orderPayment.OrderFacade;
import kr.hhplus.be.server.application.orderPayment.criteria.OrderItemCriteria;
import kr.hhplus.be.server.application.orderPayment.criteria.OrderPaymentCriteria;
import kr.hhplus.be.server.application.orderPayment.result.OrderResult;
import kr.hhplus.be.server.application.orderPayment.result.PaymentResult;
import kr.hhplus.be.server.domain.couponv2.CouponV2;
import kr.hhplus.be.server.domain.couponv2.CouponV2Repository;
import kr.hhplus.be.server.domain.couponv2.UserCouponV2;
import kr.hhplus.be.server.domain.couponv2.UserCouponV2Repository;
import kr.hhplus.be.server.domain.item.Item;
import kr.hhplus.be.server.domain.item.ItemRepository;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentRepository;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.interfaces.exception.InvalidPriceException;
import kr.hhplus.be.server.interfaces.exception.InvalidStockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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
    @Autowired CouponV2Repository couponV2Repository;
    @Autowired UserCouponV2Repository userCouponRepository;
    @Autowired PaymentRepository paymentRepository;

    User user;

    Item item1;
    Item item2;
    Item item3;

    CouponV2 coupon;
    Long userCouponId;
    List<OrderItemCriteria> orderItemCriteria;

    @Nested
    @DisplayName("신규 주문 생성 테스트")
    class create_order_test{

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

            coupon = CouponV2.createFlatCoupon(
                    "test",
                    10,
                    LocalDate.of(2025, 12, 31),
                    5_000);
            couponV2Repository.save(coupon);

            UserCouponV2 userCoupon = new UserCouponV2(user, coupon);
            userCouponRepository.save(userCoupon);
            userCouponId = userCoupon.getId();


            orderItemCriteria =
                    Arrays.asList(
                            OrderItemCriteria.of(item1.getId(), 50_000, 5),
                            OrderItemCriteria.of(item2.getId(), 10_000, 5),
                            OrderItemCriteria.of(item3.getId(), 3_000, 5)
                    );
        }



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
            UserCouponV2 userCoupon = userCouponRepository.findById(userCouponId).get();

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
    @DisplayName("결제 테스트")
    class payment_create_test {

        Long orderId1;
        Order order;
        Payment payment;


        @BeforeEach
        void setUp() {
            user = new User("Tester");
            user.chargePoint(999_999);
            userRepository.save(user);

            item1 = new Item("car", 50_000, 10);
            item2 = new Item("truck", 10_000, 50);
            item3 = new Item("food", 3_000, 5);
            itemRepository.save(item1);
            itemRepository.save(item2);
            itemRepository.save(item3);

            List<OrderItem> orderItemList = Arrays.asList(
                    new OrderItem(item1, item1.price(), 10),
                    new OrderItem(item2, item2.price(), 3),
                    new OrderItem(item3, item3.price(), 5)
            );

            orderId1 = orderRepository.save(new Order(user, orderItemList)).getId();
            order = orderRepository.save(new Order(user, orderItemList));

            payment = new Payment(order, user);
            paymentRepository.save(payment);
        }

        @Test
        @Transactional
        void 결제_저장에_성공하면_주문결제정보를_등록하고_상태를_변경한다(){
            //given
            OrderPaymentCriteria.PaymentCreate criteria = new OrderPaymentCriteria.PaymentCreate(orderId1, user.getId());

            //when
            PaymentResult.Create paymentResult = orderFacade.createPayment(criteria);
            Payment payment = paymentRepository.findById(paymentResult.getPaymentId()).get();
            Order order = orderRepository.findById(orderId1).get();

            //then
            assertEquals(order, payment.getOrder());
            assertEquals(OrderStatus.PAYMENT_WAITING, order.getOrderStatus());
            assertEquals(order.getFinalPaymentPrice(), payment.getPaymentPrice());
        }

        @Test
        @Transactional
        void 결제_처리_성공시_상품재고와_잔액이_차감된다(){
            //given
            OrderPaymentCriteria.PaymentProcess criteria = new OrderPaymentCriteria.PaymentProcess(order.getId(), user.getId(), payment.getId());
            PaymentResult.Process process = orderFacade.processPayment(criteria);

            //when
            Payment payment = paymentRepository.findById(process.getPaymentId()).get();

            assertNotNull(payment.getPaymentResponseDateTime());
            assertEquals(OrderStatus.PAYMENT_COMPLETED, order.getOrderStatus());
            assertEquals(0, item1.stock());
        }
    }

}
