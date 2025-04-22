package kr.hhplus.be.server.infrastructure.coupon;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.FlatDiscountCoupon;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CouponRepositoryImpl implements CouponRepository {

    private final CouponJpaRepository couponJpaRepository;
    private final FlatDiscountCouponJpaRepository flatDiscountCouponJpaRepository;
    public CouponRepositoryImpl(
            CouponJpaRepository couponJpaRepository,
            FlatDiscountCouponJpaRepository flatDiscountCouponJpaRepository) {
        this.couponJpaRepository = couponJpaRepository;
        this.flatDiscountCouponJpaRepository = flatDiscountCouponJpaRepository;
    }

    @Override
    public Coupon save(Coupon coupon) {
        return couponJpaRepository.save(coupon);
    }

    @Override
    public Optional<Coupon> findById(Long couponId) {
        return couponJpaRepository.findById(couponId);
    }

    @Override
    public Optional<FlatDiscountCoupon> findFlatCouponById(Long couponId) {
        return flatDiscountCouponJpaRepository.findById(couponId);
    }

    @Override
    public void flush() {
        couponJpaRepository.flush();
    }

    @Override
    public Optional<Coupon> findByIdWithPessimisticLock(Long couponId) {
        return couponJpaRepository.findByIdWithPessimisticLock(couponId);
    }
}
