package kr.hhplus.be.server.integration.support;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.item.Item;
import kr.hhplus.be.server.domain.user.User;

public class SampleValues {
    public final User user;
    public final Items items;
    public final Coupon coupon;

    public SampleValues(User user, Items items, Coupon coupon) {
        this.user = user;
        this.items = items;
        this.coupon = coupon;
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
