package kr.hhplus.be.server.domain.couponv2;

import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CouponV2Service {

    private CouponV2Repository couponV2Repository;
    private UserCouponV2Repository userCouponV2Repository;

    public CouponV2Service(
            CouponV2Repository couponV2Repository,
            UserCouponV2Repository userCouponV2Repository) {
        this.couponV2Repository = couponV2Repository;
        this.userCouponV2Repository = userCouponV2Repository;
    }

    public CouponV2 saveCoupon(CouponV2 couponV2, Integer discountRate) {
        new FlatDiscountCouponV2(couponV2, discountRate);
        return couponV2Repository.save(couponV2);
    }

    public UserCouponV2 saveUserCoupon(UserCouponV2 userCouponV2) {
        return userCouponV2Repository.save(userCouponV2);
    }

    @Transactional(readOnly = true)
    public CouponV2 findById(Long couponId) {
        return couponV2Repository.findById(couponId)
                .orElseThrow(NoResultException::new);
    }

    @Transactional(readOnly = true)
    public boolean isAlreadyIssuedCoupon(Long userId, Long couponId) {
        return userCouponV2Repository.isAlreadyIssuedCoupon(userId, couponId);
    }

    @Transactional(readOnly = true)
    public UserCouponV2 findUserCouponById(Long userCouponId) {
        return userCouponV2Repository.findById(userCouponId)
                .orElseThrow(NoResultException::new);
    }
}
