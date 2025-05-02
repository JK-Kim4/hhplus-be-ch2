package kr.hhplus.be.server.infrastructure.coupon;

import kr.hhplus.be.server.domain.coupon.userCoupon.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserCouponJpaRepository extends JpaRepository<UserCoupon, Long> {

    @Query("select uc from UserCoupon uc " +
            "where uc.coupon.id = :couponId " +
            "and uc.user.id = :userId")
    Optional<UserCoupon> findByCouponIdAndUserId(
            @Param("couponId") Long couponId,
            @Param("userId") Long userId);

    @Query( "select count(uc) > 0 from UserCoupon uc " +
            "where uc.user.id = :userId " +
            "and uc.coupon.id = :couponId")
    boolean isAlreadyIssuedCoupon(
            @Param("userId") Long userId,
            @Param("couponId")Long couponId);

    @Query("select uc from UserCoupon uc where uc.user.id = :userId")
    List<UserCoupon> findByUserId(@Param("userId") Long userId);

    @Query("select count(uc) from UserCoupon uc where uc.coupon.id = :couponId")
    Integer countByCouponId(@Param("couponId") Long couponId);

}
