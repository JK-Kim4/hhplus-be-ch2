package kr.hhplus.be.server.infrastructure.coupon;

import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.coupon.UserCouponRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserCouponRepositoryImpl implements UserCouponRepository {

    private final UserCouponJpaRepository userCouponJpaRepository;
    public UserCouponRepositoryImpl(UserCouponJpaRepository userCouponJpaRepository) {
        this.userCouponJpaRepository = userCouponJpaRepository;
    }

    @Override
    public UserCoupon save(UserCoupon userCoupon) {
        return userCouponJpaRepository.save(userCoupon);
    }

    @Override
    public Optional<UserCoupon> findByUserIdAndCouponId(Long userId, Long couponId) {
        return userCouponJpaRepository.findByUserIdAndCouponId(userId, couponId);
    }

    @Override
    public boolean isAlreadyIssuedCoupon(Long userId, Long couponId) {
        return userCouponJpaRepository.isAlreadyIssuedCoupon(userId, couponId);
    }

    @Override
    public Optional<UserCoupon> findById(Long userCouponId) {
        return userCouponJpaRepository.findById(userCouponId);
    }
}
