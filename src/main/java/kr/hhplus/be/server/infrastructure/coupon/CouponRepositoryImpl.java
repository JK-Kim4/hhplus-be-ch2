package kr.hhplus.be.server.infrastructure.coupon;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class CouponRepositoryImpl implements CouponRepository {

    private final CouponJpaRepository couponJpaRepository;

    public CouponRepositoryImpl(CouponJpaRepository couponJpaRepository) {
        this.couponJpaRepository = couponJpaRepository;
    }

    @Override
    public void flush() {
        couponJpaRepository.flush();
    }

    @Override
    public Coupon save(Coupon coupon) {
        return couponJpaRepository.save(coupon);
    }

    @Override
    public void saveAll(List<Coupon> coupons) {
        couponJpaRepository.saveAll(coupons);
    }

    @Override
    public Optional<Coupon> findById(Long couponId) {
        return couponJpaRepository.findById(couponId);
    }

    @Override
    public Optional<Coupon> findByIdWithPessimisticLock(Long couponId) {
        return couponJpaRepository.findByIdWithPessimisticLock(couponId);
    }

    @Override
    public List<Coupon> getCouponsWithRemainingQuantity() {
        return couponJpaRepository.getCouponsWithRemainingQuantity();
    }

    @Override
    public Integer getAvailableQuantityByCouponId(Long couponId) {
        return couponJpaRepository.getAvailableQuantityByCouponId(couponId);
    }

    @Override
    public List<Coupon> findIssuableCoupons(LocalDate now) {
        return couponJpaRepository.findIssuableCoupons(now);
    }

    @Override
    public List<Coupon> findExpiredCoupons(LocalDate now) {
        return couponJpaRepository.findExpiredCoupons(now);
    }
}
