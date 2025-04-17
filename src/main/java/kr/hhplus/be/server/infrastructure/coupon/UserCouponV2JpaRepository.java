package kr.hhplus.be.server.infrastructure.coupon;

import kr.hhplus.be.server.domain.couponv2.UserCouponV2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserCouponV2JpaRepository extends JpaRepository<UserCouponV2, Long> {
    Optional<UserCouponV2> findByUserIdAndCouponId(Long userId, Long couponId);

    @Query( "select count(uc) > 0 from UserCouponV2 uc " +
            "where uc.user.id = :userId " +
            "and uc.coupon.id = :couponId")
    boolean isAlreadyIssuedCoupon(
            @Param("userId") Long userId,
            @Param("couponId")Long couponId);
}
