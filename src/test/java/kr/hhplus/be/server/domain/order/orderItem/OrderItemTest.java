package kr.hhplus.be.server.domain.order.orderItem;

import kr.hhplus.be.server.domain.item.Item;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.interfaces.exception.NotEnoughStockException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OrderItemTest {

    Integer defaultOrderQuantity = 500;
    Integer defaultItemPrice = 15000;
    Integer defaultItemStock = 500;
    Item item = new Item("test", defaultItemPrice, defaultItemStock);
    OrderItem orderItem = new OrderItem(new Order(), item, defaultOrderQuantity);

    @Test
    @DisplayName("주문 수량에 따라 상품 결제 금액을 계산한다.")
    void calculate_order_item_price_test(){
        //then
        assertEquals(defaultItemPrice * defaultOrderQuantity, orderItem.calculatePrice());
    }

    @DisplayName("주문 가능 재고가 부족할 경우 가격 계산시 오류를 반환한다.")
    @Test
    void calculate_order_price_exception_test(){
        //given
        OrderItem orderItem = new OrderItem(new Order(), item, defaultOrderQuantity + 1);

        //when

        //then
        assertThrows(NotEnoughStockException.class,
                orderItem::calculatePrice);
    }

    @Test
    @DisplayName("주문 수량만큼 상품 재고를 차감한다.")
    void decrease_item_stock_test(){
        //when
        orderItem.decreaseItemStock();

        //then
        assertEquals((defaultItemStock - defaultOrderQuantity), item.getStock());
    }


    @Test
    @DisplayName("재고가 충분할 경우 true를 리턴")
    void canOrder_returnsTrue_whenStockIsEnough() {
        assertTrue(orderItem.canOrder());
    }

    @Test
    @DisplayName("재고가 부족할 경우 NotEnoughStockException 오류 응답")
    void canOrder_throwsException_whenStockIsNotEnough() {
        OrderItem orderItem = new OrderItem(new Order(), item, defaultOrderQuantity+1);

        assertThrows(NotEnoughStockException.class, orderItem::canOrder);
    }
}
