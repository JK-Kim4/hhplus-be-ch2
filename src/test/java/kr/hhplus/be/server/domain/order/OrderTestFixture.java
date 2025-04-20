package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.item.Item;
import kr.hhplus.be.server.domain.user.User;

import java.util.Arrays;
import java.util.List;

public class OrderTestFixture {

    public static Order createTestOrder(){
        User user = User.createWithName("test");
        List<OrderItem> orderItems = Arrays.asList(
                createOrderItemFixture("car", 3_000, 10),
                createOrderItemFixture("book", 2_000, 10),
                createOrderItemFixture("food", 5_000, 10)
        );

        //when
        return new Order(user, orderItems);
    }

    public static Order createTestOrderWithUser(User user){
        List<OrderItem> orderItems = Arrays.asList(
                createOrderItemFixture("car", 3_000, 10),
                createOrderItemFixture("book", 2_000, 10),
                createOrderItemFixture("food", 5_000, 10)
        );

        //when
        return new Order(user, orderItems);
    }

    public static OrderItem createOrderItemFixture(String name, Integer price, Integer quantity) {
        Item item  = Item.createWithNameAndPriceAndStock(name, price, 9999);
        return OrderItem.createWithItemAndPriceAndQuantity(item, price, quantity);
    }

    public static OrderItem createOrderItemWithItemAndPriceAndQuantity(Item item, Integer price, Integer quantity) {
        return OrderItem.createWithItemAndPriceAndQuantity(item, price, quantity);
    }
}
