package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.NoResultException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CouponServiceTest {

    @Mock
    CouponRepository couponRepository;

    @Mock
    UserCouponRepository userCouponRepository;

    @InjectMocks
    CouponService couponService;

    @Test
    void 쿠폰이_존재하지_않으면_예외가_발생한다() {
        // given
        Long couponId = 1L;
        Long userId = 100L;
        CouponCommand.Issue command =
                CouponCommand.Issue.builder()
                    .couponId(couponId)
                    .userId(userId)
                    .build();

        when(couponRepository.findById(couponId)).thenReturn(Optional.empty());

        // expect
        assertThatThrownBy(() -> couponService.issueUserCoupon(command))
                .isInstanceOf(NoResultException.class);
    }

}
