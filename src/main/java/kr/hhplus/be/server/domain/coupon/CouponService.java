package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Service;

@Service
public class CouponService {

    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;

    public CouponService(
            CouponRepository couponRepository,
            UserCouponRepository userCouponRepository) {
        this.couponRepository = couponRepository;
        this.userCouponRepository = userCouponRepository;
    }

    public CouponInfo.Coupon findById(Long couponId){
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(NoResultException::new);

        return CouponInfo.Coupon.from(coupon);
    }

    public CouponInfo.Coupon findByIdWithPessimisticLock(Long couponId){
        Coupon coupon = couponRepository.findByIdWithPessimisticLock(couponId)
                .orElseThrow(NoResultException::new);

        return CouponInfo.Coupon.from(coupon);
    }

    public CouponInfo.UserCouponOptional findUserCouponByCouponIdAndUserId(Long couponId, Long userId){
        return CouponInfo.UserCouponOptional.from(userCouponRepository.findByCouponIdAndUserId(couponId, userId));
    }

    public CouponInfo.Issue issueUserCoupon(CouponCommand.Issue command){
        Coupon coupon = couponRepository.findById(command.couponId)
                .orElseThrow(NoResultException::new);

        UserCoupon userCoupon = UserCoupon.issue(coupon, command.getUserId());
        userCouponRepository.save(userCoupon);

        return CouponInfo.Issue.from(userCoupon);
    }
}
