package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.NoResultException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
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
        assertThatThrownBy(() -> couponService.issue(command))
                .isInstanceOf(NoResultException.class);
    }

    @Test
    void 이미_발급된_쿠폰이면_예외가_발생한다() {
        // given
        Long couponId = 1L;
        Long userId = 100L;
        Coupon coupon = Coupon.create(
                "정액 할인",
                5,
                DiscountPolicy.FLAT,
                BigDecimal.valueOf(5000),
                LocalDate.now().plusDays(2)
        );

        UserCoupon existing = UserCoupon.issue(coupon, userId);
        CouponCommand.Issue command =
                CouponCommand.Issue.builder()
                        .couponId(couponId)
                        .userId(userId)
                        .build();

        when(couponRepository.findById(couponId)).thenReturn(Optional.of(coupon));
        when(userCouponRepository.findByCouponIdAndUserId(couponId, userId)).thenReturn(Optional.of(existing));

        // expect
        assertThatThrownBy(() -> couponService.issue(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 발급된 쿠폰");
    }

}
