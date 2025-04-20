package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.domain.item.Item;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.order.OrderTestFixture;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserTestFixture;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentTest {

    @Test
    void 전달받은_사용자정보가_결제사용자와_일치하지않을경우_isPayable은_IllegalArgumentException(){
        //given
        Payment payment = PaymentTestFixture.createTestPayment();
        User user = UserTestFixture.createTestUser();

        //when//then
        assertThrows(IllegalArgumentException.class,() -> payment.isPayable(user));
    }

    @Test
    void 전달받은_사용자의_잔고가_부족할경우_isPayable은_false를_리턴(){
        //given
        Payment payment = PaymentTestFixture.createTestPayment();
        User user = payment.getUser();

        //when
        user.deductPoint(user.point());

        // then
        assertFalse(payment.isPayable(user));
    }

    @Test
    void 결제에_성공하면_상품재고와_사용자잔액이_차감되고_주문상태가_갱신된다(){
        //given
        Item car  = Item.createWithNameAndPriceAndStock("car", 3_000, 10);
        Item book  = Item.createWithNameAndPriceAndStock("book", 2_000, 10);
        Item food  = Item.createWithNameAndPriceAndStock("food", 5_000, 10);

        OrderItem carOi = OrderTestFixture.createOrderItemWithItemAndPriceAndQuantity(
                car,
                3_000,
                5);
        OrderItem bookOi = OrderTestFixture.createOrderItemWithItemAndPriceAndQuantity(
                book,
                2_000,
                5
        );
        OrderItem foodOi = OrderTestFixture.createOrderItemWithItemAndPriceAndQuantity(
                food,
                5_000,
                1
        );
        List<OrderItem> orderItems = Arrays.asList(carOi, bookOi, foodOi);
        User user = UserTestFixture.createTestUser();
        user.chargePoint(100_000);
        Order order = new Order(user, orderItems);
        Integer finalPrice = order.getFinalPaymentPrice();
        Payment payment = PaymentTestFixture.creatTestPaymentWithOrderAndUser(order);

        //when
        payment.pay(user);

        //then
        assertEquals(100_000 - finalPrice, user.point());
        assertEquals(OrderStatus.PAYMENT_COMPLETED, order.getOrderStatus());
        assertEquals(5, car.stock());
        assertEquals(5, book.stock());
        assertEquals(9, food.stock());


    }





}
