package kr.hhplus.be.server.infrastructure.coupon;

import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.domain.coupon.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CouponJpaRepository extends JpaRepository<Coupon, Long> {


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Coupon c where c.id = :couponId")
    Optional<Coupon> findByIdWithPessimisticLock(@Param("couponId") Long couponId);

    @Query("select c from Coupon c where c.quantity > 0")
    List<Coupon> getCouponsWithRemainingQuantity();

    @Query("select c.quantity from Coupon c where c.id = :couponId")
    Integer getAvailableQuantityByCouponId(@Param("couponId") Long couponId);

    @Query("select c from Coupon c where c.quantity > 0 and c.expireDate >= :now")
    List<Coupon> findIssuableCoupons(@Param("now") LocalDate now);

    @Query("select c from Coupon c where c.quantity <= 0 and c.expireDate < :now")
    List<Coupon> findExpiredCoupons(LocalDate now);
}
