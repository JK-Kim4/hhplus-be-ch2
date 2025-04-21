package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.NoResultException;
import kr.hhplus.be.server.domain.coupon.userCoupon.UserCoupon;
import kr.hhplus.be.server.domain.coupon.userCoupon.UserCouponRepository;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderTestFixture;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserTestFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CouponServiceTest {

    @Mock
    private CouponRepository couponRepository;
    @Mock
    private UserCouponRepository userCouponRepository;
    @InjectMocks
    private CouponService couponService;

    @Test
    void 고유번호를_전달받아_쿠폰_상세정보를_조회할때_쿠폰이_존재하지않으면_NoResultException() {
        //given
        when(couponRepository.findById(1L)).thenReturn(Optional.empty());
        Long findCouponId = 1L;


        //when//then
        assertThrows(NoResultException.class,
                () -> couponService.findById(findCouponId));
    }

    @Test
    void 고유번호를_전달받아_발급되_사용자쿠폰의_상세정보를_조회할떄_존재하지않으면_return_null(){
        //given
        when(userCouponRepository.findById(1L)).thenReturn(Optional.empty());

        //when//then
        assertNull(couponService.findUserCouponById(1L));
    }

    @Test
    void 쿠폰을_적용하여_할인금액을_계산하고_쿠폰정보를_개신한다(){
        //given
        User user = UserTestFixture.createTestUser();
        Order order = OrderTestFixture.createTestOrderWithUser(user);
        Coupon coupon = CouponTestFixture.createTestCouponWithDiscountPrice(5000);
        UserCoupon userCoupon = UserCoupon.create(user, coupon);

        //when
        couponService.applyCouponToOrder(userCoupon, order);

        //then
        assertEquals(OrderTestFixture.TestOrder.TOTAL_PRICE, order.getTotalPrice());
        assertEquals(OrderTestFixture.TestOrder.TOTAL_PRICE - 5000, order.getFinalPaymentPrice());
        assertEquals(order, userCoupon.getAppliedOrder());
    }

}
