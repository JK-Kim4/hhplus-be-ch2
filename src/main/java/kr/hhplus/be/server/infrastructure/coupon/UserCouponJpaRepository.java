package kr.hhplus.be.server.infrastructure.coupon;

import kr.hhplus.be.server.domain.coupon.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserCouponJpaRepository extends JpaRepository<UserCoupon, Long> {
    Optional<UserCoupon> findByUserIdAndCouponId(Long userId, Long couponId);

    @Query( "select count(uc) > 0 from UserCoupon uc " +
            "where uc.user.id = :userId " +
            "and uc.coupon.id = :couponId")
    boolean isAlreadyIssuedCoupon(
            @Param("userId") Long userId,
            @Param("couponId")Long couponId);
}
