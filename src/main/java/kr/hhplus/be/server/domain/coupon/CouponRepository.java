package kr.hhplus.be.server.domain.coupon;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface CouponRepository {

    void flush();

    Coupon save(Coupon coupon);

    void saveAll(List<Coupon> coupons);

    Optional<Coupon> findById(Long couponId);

    Optional<Coupon> findByIdWithPessimisticLock(Long couponId);

    List<Coupon> getCouponsWithRemainingQuantity();

    Integer getAvailableQuantityByCouponId(Long couponId);

    List<Coupon> findIssuableCoupons(LocalDate now);

    List<Coupon> findExpiredCoupons(LocalDate now);
}
