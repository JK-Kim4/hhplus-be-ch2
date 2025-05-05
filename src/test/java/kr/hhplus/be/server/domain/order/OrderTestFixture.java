package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.item.Item;
import kr.hhplus.be.server.domain.user.User;

import java.util.Arrays;
import java.util.List;

public class OrderTestFixture {

    public static Order createTestOrder(){
        User user = User.createWithName("test");
        user.chargePoint(500_000);
        Item car = Item.createWithNameAndPriceAndStock("car", 3_000, 50);
        Item book = Item.createWithNameAndPriceAndStock("book", 2_000, 50);
        Item food = Item.createWithNameAndPriceAndStock("food", 5_000, 50);

        List<OrderItem> orderItems = Arrays.asList(
                createOrderItemWithItemAndPriceAndQuantity(car, 3_000, 10),
                createOrderItemWithItemAndPriceAndQuantity(book, 2_000, 10),
                createOrderItemWithItemAndPriceAndQuantity(food, 5_000, 10)
        );

        //when
        return Order.createWithItems(user, orderItems);
    }

    public static Order createTestOrderWithUser(User user){
        List<OrderItem> orderItems = Arrays.asList(
                createOrderItemFixture("car", 3_000, 10),
                createOrderItemFixture("book", 2_000, 10),
                createOrderItemFixture("food", 5_000, 10)
        );

        //when
        return Order.createWithItems(user, orderItems);
    }

    public static OrderItem createOrderItemFixture(String name, Integer price, Integer quantity) {
        Item item  = Item.createWithNameAndPriceAndStock(name, price, 9999);
        return OrderItem.createWithItemAndPriceAndQuantity(item, price, quantity);
    }

    public static OrderItem createOrderItemWithItemAndPriceAndQuantity(Item item, Integer price, Integer quantity) {
        return OrderItem.createWithItemAndPriceAndQuantity(item, price, quantity);
    }

    public static class TestOrder{
        public static Integer TOTAL_PRICE = 100_000;
        public static Integer DEFAULT_USER_BALANCE = 500_000;
        public static Integer TOTAL_ORDER_ITEM_SIZE = 3;
    }
}
