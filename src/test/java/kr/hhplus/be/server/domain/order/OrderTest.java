package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.item.Item;
import kr.hhplus.be.server.domain.order.orderItem.OrderItem;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.interfaces.exception.OrderMismatchException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OrderTest {

    Integer defaultOrderQuantity = 500;
    Integer defaultItemPrice = 15000;
    Integer defaultItemStock = 500;
    Order order = new Order(1L, new User(1L, "tester"), new Payment());
    List<OrderItem> orderItems = new ArrayList<>();

    @Nested
    @DisplayName("주문 상품 가격 계산")
    class OrderPriceCalculationTest{

        @BeforeEach
        void setUp(){
            OrderItem orderItem1 = new OrderItem(
                    order,
                    new Item("test item 1", defaultItemPrice, defaultItemStock),
                    defaultOrderQuantity);
            OrderItem orderItem2 = new OrderItem(
                    order,
                    new Item("test item 2", defaultItemPrice, defaultItemStock),
                    defaultOrderQuantity);
            OrderItem orderItem3 = new OrderItem(
                    order,
                    new Item("test item 3", defaultItemPrice, defaultItemStock),
                    defaultOrderQuantity);
            orderItems.add(orderItem1);
            orderItems.add(orderItem2);
            orderItems.add(orderItem3);
        }

        @Test
        @DisplayName("주문 상품의 총 가격을 계산한다")
        void calculate_order_total_price_test(){
            order.calculateTotalPrice(orderItems);
            Integer sum = orderItems.stream().mapToInt(OrderItem::calculatePrice).sum();

            //then
            assertEquals(sum, order.getTotalPrice());
        }

        @Test
        @DisplayName("주문 상품의 상위 주문 정보가 동일하지 않을 경우 오류를 반환한다.")
        void calculate_order_total_price_test_throw_exception_not_equals_order(){
            //given
            OrderItem mismatchOrder = new OrderItem(
                    new Order(),
                    new Item("test item 2", defaultItemPrice, defaultItemStock),
                    defaultOrderQuantity);
            orderItems.add(mismatchOrder);

            //when
            OrderMismatchException exception = assertThrows(OrderMismatchException.class,
                    () -> order.calculateTotalPrice(orderItems));

            //then
            assertEquals(OrderMismatchException.DEFAULT, exception.getMessage());
        }

    }


}
