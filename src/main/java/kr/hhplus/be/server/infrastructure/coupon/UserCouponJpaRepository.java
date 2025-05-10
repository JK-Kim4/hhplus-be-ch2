package kr.hhplus.be.server.infrastructure.coupon;

import io.lettuce.core.dynamic.annotation.Param;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserCouponJpaRepository extends JpaRepository<UserCoupon, Long> {

    @Query("select uc from UserCoupon uc where uc.coupon.id = :couponId and uc.userId = :userId")
    Optional<UserCoupon> findByCouponIdAndUserId(@Param("couponId") Long couponId, @Param("userId") Long userId);

}
