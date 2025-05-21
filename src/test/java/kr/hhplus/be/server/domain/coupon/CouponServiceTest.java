package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.NoResultException;
import kr.hhplus.be.server.support.domainSupport.CouponDomainSupporter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

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


    @Test
    void 선착순쿠폰_발급을위해_쿠폰고유번호와_발급대상자고유번호목록을_전달받아_쿠폰발급을_실행한다(){
        //given
        Long couponId = 1L;
        Coupon coupon = CouponDomainSupporter.수량정보를_전달받아_유효_테스트쿠폰_생성(10);
        UserCoupon userCoupon = CouponDomainSupporter.쿠폰정보를_전달받아_사용자쿠폰_생성(coupon);
        when(couponRepository.findById(couponId)).thenReturn(Optional.of(coupon));
        when(userCouponRepository.save(any())).thenReturn(userCoupon);
        List<Long> applicantIds = List.of(1L, 2L, 3L);

        //when
        couponService.issueCouponToApplicantsV2(couponId, applicantIds);

        //then
        verify(userCouponRepository, times(3)).save(any());
    }
}
