package kr.hhplus.be.server.infrastructure.coupon;

import kr.hhplus.be.server.domain.couponv2.UserCouponV2;
import kr.hhplus.be.server.domain.couponv2.UserCouponV2Repository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserCouponV2RepositoryImpl implements UserCouponV2Repository {

    private final UserCouponV2JpaRepository userCouponV2JpaRepository;
    public UserCouponV2RepositoryImpl(UserCouponV2JpaRepository userCouponV2JpaRepository) {
        this.userCouponV2JpaRepository = userCouponV2JpaRepository;
    }

    @Override
    public UserCouponV2 save(UserCouponV2 userCouponV2) {
        return userCouponV2JpaRepository.save(userCouponV2);
    }

    @Override
    public Optional<UserCouponV2> findByUserIdAndCouponId(Long userId, Long couponId) {
        return userCouponV2JpaRepository.findByUserIdAndCouponId(userId, couponId);
    }

    @Override
    public boolean isAlreadyIssuedCoupon(Long userId, Long couponId) {
        return userCouponV2JpaRepository.isAlreadyIssuedCoupon(userId, couponId);
    }

    @Override
    public Optional<UserCouponV2> findById(Long userCouponId) {
        return userCouponV2JpaRepository.findById(userCouponId);
    }
}
