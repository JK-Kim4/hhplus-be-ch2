package kr.hhplus.be.server.domain.couponv2;

import kr.hhplus.be.server.domain.FakeOrder;
import kr.hhplus.be.server.domain.FakeUser;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.user.User;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
public class UserCouponV2Test {

    User user = new FakeUser(1L, "tester");
    Order order = new FakeOrder(20_000);
    CouponV2 coupon = new CouponV2("test", CouponType.FLAT,10,
            LocalDate.of(2025, 12, 31), LocalDate.of(2025,1,1));
    FlatDiscountCouponV2 flatCoupon = new FlatDiscountCouponV2(coupon, 50_000);



}
