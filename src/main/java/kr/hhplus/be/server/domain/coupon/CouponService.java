package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public CouponInfo.Issue issue(CouponCommand.Issue command){
        Coupon coupon = couponRepository.findById(command.getCouponId())
                .orElseThrow(NoResultException::new);

        Optional<UserCoupon> existing = userCouponRepository.findByCouponIdAndUserId(command.getCouponId(), command.getUserId());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("이미 발급된 쿠폰입니다.");
        }

        UserCoupon userCoupon = UserCoupon.issue(coupon, command.getUserId());
        userCouponRepository.save(userCoupon);

        return CouponInfo.Issue.from(userCoupon);
    }
}
