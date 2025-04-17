package kr.hhplus.be.server.infrastructure.coupon;

import kr.hhplus.be.server.domain.couponv2.CouponV2;
import kr.hhplus.be.server.domain.couponv2.CouponV2Repository;
import kr.hhplus.be.server.domain.couponv2.FlatDiscountCouponV2;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CouponV2RepositoryImpl implements CouponV2Repository {

    private final CouponV2JpaRepository couponV2JpaRepository;
    private final FlatDiscountCouponJpaRepository flatDiscountCouponJpaRepository;
    public CouponV2RepositoryImpl(
            CouponV2JpaRepository couponV2JpaRepository,
            FlatDiscountCouponJpaRepository flatDiscountCouponJpaRepository) {
        this.couponV2JpaRepository = couponV2JpaRepository;
        this.flatDiscountCouponJpaRepository = flatDiscountCouponJpaRepository;
    }

    @Override
    public CouponV2 save(CouponV2 couponV2) {
        return couponV2JpaRepository.save(couponV2);
    }

    @Override
    public Optional<CouponV2> findById(Long couponId) {
        return couponV2JpaRepository.findById(couponId);
    }

    @Override
    public Optional<FlatDiscountCouponV2> findFlatCouponById(Long couponId) {
        return flatDiscountCouponJpaRepository.findById(couponId);
    }
}
