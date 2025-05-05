package kr.hhplus.be.server.domain.coupon;

import java.util.Optional;


public interface CouponRepository {

    void flush();

    Coupon save(Coupon coupon);

    Optional<Coupon> findById(Long couponId);

    Optional<Coupon> findByIdWithPessimisticLock(Long couponId);
}
