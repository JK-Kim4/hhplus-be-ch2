package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.NoResultException;
import kr.hhplus.be.server.application.coupon.CouponCommandService;
import kr.hhplus.be.server.application.coupon.CouponQueryService;
import kr.hhplus.be.server.domain.coupon.userCoupon.UserCouponRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CouponServiceTest {

    @Mock
    private CouponRepository couponRepository;
    @Mock
    private UserCouponRepository userCouponRepository;
    @InjectMocks
    private CouponCommandService couponCommandService;
    @InjectMocks
    private CouponQueryService couponQueryService;

    @Test
    void 고유번호를_전달받아_쿠폰_상세정보를_조회할때_쿠폰이_존재하지않으면_NoResultException() {
        //given
        when(couponRepository.findById(1L)).thenReturn(Optional.empty());
        Long findCouponId = 1L;


        //when//then
        assertThrows(NoResultException.class,
                () -> couponQueryService.findById(findCouponId));
    }

    @Test
    void 고유번호를_전달받아_발급되_사용자쿠폰의_상세정보를_조회할떄_존재하지않으면_return_null(){
        //given
        when(userCouponRepository.findById(1L)).thenReturn(Optional.empty());

        //when//then
        assertNull(couponQueryService.findUserCouponById(1L));
    }

}
