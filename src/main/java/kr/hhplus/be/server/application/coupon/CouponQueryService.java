package kr.hhplus.be.server.application.coupon;

import jakarta.persistence.NoResultException;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.userCoupon.UserCoupon;
import kr.hhplus.be.server.domain.coupon.userCoupon.UserCouponRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CouponQueryService {

    private CouponRepository couponRepository;
    private UserCouponRepository userCouponRepository;

    public CouponQueryService(
            CouponRepository couponRepository,
            UserCouponRepository userCouponRepository) {
        this.couponRepository = couponRepository;
        this.userCouponRepository = userCouponRepository;
    }


    public Coupon findById(Long couponId) {
        return couponRepository.findById(couponId)
                .orElseThrow(NoResultException::new);
    }

    public UserCoupon findUserCouponById(Long userCouponId) {
        return userCouponRepository.findById(userCouponId)
                .orElse(null);
    }

    public List<CouponInfo.UserCouponInfo> findByUserId(Long userId) {
        return CouponInfo.UserCouponInfo.fromList(userCouponRepository.findByUserId(userId));
    }

    public Optional<UserCoupon> findByCouponIdAndUserId(Long couponId, Long userId) {
        return userCouponRepository.findByCouponIdAndUserId(couponId, userId);
    }
}
