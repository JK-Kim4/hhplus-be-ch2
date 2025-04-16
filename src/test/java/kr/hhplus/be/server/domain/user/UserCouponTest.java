package kr.hhplus.be.server.domain.user;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponTemplate;
import kr.hhplus.be.server.domain.coupon.FlatDiscountCoupon;
import kr.hhplus.be.server.domain.user.userCoupon.UserCoupon;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserCouponTest {

    @Test
    void 인자가_누락될경우_쿠폰생성시_오류를_반환한다(){

        assertThrows(IllegalArgumentException.class, () ->
            new UserCoupon(null, null));
        assertThrows(IllegalArgumentException.class, () ->
            new UserCoupon(mock(User.class), null));
        assertThrows(IllegalArgumentException.class, () ->
            new UserCoupon(null, mock(Coupon.class)));
    }

    @Test
    void 사용기간이_만료된경우_쿠폰생성시_오류를_반환한다(){
        //given
        User user = mock(User.class);
        Coupon coupon = mock(Coupon.class);
        when(coupon.isBeforeExpiredDate(any())).thenReturn(false);

        //when
        IllegalArgumentException illegalArgumentException
                = assertThrows(IllegalArgumentException.class, () -> new UserCoupon(user, coupon));

        //then
        assertEquals("만료된 쿠폰입니다.", illegalArgumentException.getMessage());
    }

    @Test
    void 재고가_부족하면_쿠폰생성시_오류를_반환한다(){
        //given
        User user = mock(User.class);
        Coupon coupon = mock(Coupon.class);
        when(coupon.isBeforeExpiredDate(any())).thenReturn(true);
        when(coupon.hasEnoughQuantity()).thenReturn(false);

        //when
        IllegalArgumentException illegalArgumentException
                = assertThrows(IllegalArgumentException.class, () -> new UserCoupon(user, coupon));

        //then
        assertEquals("재고가 소진되었습니다.", illegalArgumentException.getMessage());
    }

    @Test
    void 이미_발급된_쿠폰은_생성시_오류를_반환한다(){
        User user = mock(User.class);
        Coupon coupon = mock(Coupon.class);
        when(coupon.isBeforeExpiredDate(any())).thenReturn(true);
        when(coupon.hasEnoughQuantity()).thenReturn(true);
        when(user.isAlreadyIssuedCoupon(any())).thenReturn(true);

        //when
        IllegalArgumentException illegalArgumentException
                = assertThrows(IllegalArgumentException.class, () -> new UserCoupon(user, coupon));

        //then
        assertEquals("이미 발급된 쿠폰입니다.",illegalArgumentException.getMessage());
    }

    @Test
    void 쿠폰_발급_시용자_일치여부를_검사한다(){
        User user = mock(User.class);
        Coupon coupon = mock(Coupon.class);
        Mockito.when(user.getId()).thenReturn(1L);
        when(coupon.isBeforeExpiredDate(any())).thenReturn(true);
        when(coupon.hasEnoughQuantity()).thenReturn(true);
        when(user.isAlreadyIssuedCoupon(any())).thenReturn(false);
        UserCoupon userCoupon = new UserCoupon(user, coupon);

        assertTrue(userCoupon.isCouponOwner(1L));
    }

    @Test
    void 쿠폰_할인을_적용하여_최종금액을_계산한다(){
        Integer primer = 10_000;
        CouponTemplate template = CouponTemplate
                .builder()
                .id(1L)
                .remainingQuantity(10)
                .expireDateTime(LocalDateTime.now().plusDays(10))
                .build();
        Coupon coupon = new FlatDiscountCoupon(template, 3000);

        System.out.println(coupon.getExpireDateTime());
        User user = mock(User.class);
        when(user.isAlreadyIssuedCoupon(any())).thenReturn(false);

        UserCoupon userCoupon = new UserCoupon(user, coupon);

        assertEquals(7000, userCoupon.discount(primer));
    }
}
