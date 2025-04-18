package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CouponService {

    private CouponRepository couponRepository;
    private UserCouponRepository userCouponRepository;

    public CouponService(
            CouponRepository couponRepository,
            UserCouponRepository userCouponRepository) {
        this.couponRepository = couponRepository;
        this.userCouponRepository = userCouponRepository;
    }

    public Coupon saveCoupon(Coupon coupon, Integer discountRate) {
        new FlatDiscountCoupon(coupon, discountRate);
        return couponRepository.save(coupon);
    }

    public UserCoupon saveUserCoupon(UserCoupon userCoupon) {
        return userCouponRepository.save(userCoupon);
    }

    @Transactional(readOnly = true)
    public Coupon findById(Long couponId) {
        return couponRepository.findById(couponId)
                .orElseThrow(NoResultException::new);
    }

    @Transactional(readOnly = true)
    public boolean isAlreadyIssuedCoupon(Long userId, Long couponId) {
        return userCouponRepository.isAlreadyIssuedCoupon(userId, couponId);
    }

    @Transactional(readOnly = true)
    public UserCoupon findUserCouponById(Long userCouponId) {
        return userCouponRepository.findById(userCouponId)
                .orElseThrow(NoResultException::new);
    }
}
