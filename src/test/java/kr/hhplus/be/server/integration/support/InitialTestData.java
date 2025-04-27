package kr.hhplus.be.server.integration.support;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.item.Item;
import kr.hhplus.be.server.domain.item.ItemRepository;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class InitialTestData {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    CouponRepository couponRepository;

    public SampleValues load() {
        User user = createUser();
        SampleValues.Items items = createItems();
        Coupon coupon = createCoupon();

        userRepository.flush();
        itemRepository.flush();
        couponRepository.flush();

        return new SampleValues(user, items, coupon);
    }

    private User createUser() {
        User user = User.createWithName("tester");
        user.chargePoint(500_000);
        return userRepository.save(user);
    }

    private SampleValues.Items createItems() {
        Item car = itemRepository.save(Item.createWithNameAndPriceAndStock("car", 3_000, 999));
        Item truck = itemRepository.save(Item.createWithNameAndPriceAndStock("truck", 2_000, 999));
        Item book = itemRepository.save(Item.createWithNameAndPriceAndStock("book", 5_000, 999));
        return new SampleValues.Items(car, truck, book);
    }

    private Coupon createCoupon() {
        Coupon coupon = Coupon.createFlatCoupon(
                "100장 선착순 5000원 할인 쿠폰",
                100,
                LocalDate.now().plusDays(10),
                5_000
        );
        return couponRepository.save(coupon);
    }


}
