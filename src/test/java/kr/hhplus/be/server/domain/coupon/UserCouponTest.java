package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.userCoupon.UserCoupon;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserCouponTest {


    @Test
    void 쿠폰_사용가능여부_검사(){
        Coupon coupon = mock(Coupon.class);
        User user = mock(User.class);
        when(user.getId()).thenReturn(10L);
        UserCoupon couponUser = new UserCoupon(user, coupon);

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> couponUser.isCouponOwner(2L));
    }


}
