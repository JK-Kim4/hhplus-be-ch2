package kr.hhplus.be.server.integration.support;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.item.Item;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.user.User;

import java.util.Arrays;
import java.util.List;

public class SampleValues {
    public final User user;
    public final Items items;
    public final Coupon coupon;

    public SampleValues(User user, Items items, Coupon coupon) {
        this.user = user;
        this.items = items;
        this.coupon = coupon;
    }

    public Order createSampleOrderWithUser(User user) {
        List<OrderItem> orderItems = Arrays.asList(
                OrderItem.createWithItemAndPriceAndQuantity(
                        items.truck, items.truck.price(), 1),
                OrderItem.createWithItemAndPriceAndQuantity(
                        items.car, items.car.price(), 2),
                OrderItem.createWithItemAndPriceAndQuantity(
                        items.book, items.book.price(), 3)
        );

        return Order.createWithItems(user, orderItems);
    }

    public static class Items {
        public final Item car;
        public final Item truck;
        public final Item book;

        public Items(Item car, Item truck, Item book) {
            this.car = car;
            this.truck = truck;
            this.book = book;
        }
    }

}
