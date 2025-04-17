package kr.hhplus.be.server.domain.couponv2;

import java.util.Optional;

public interface CouponV2Repository {

    Optional<CouponV2> findById(Long couponId);

    Optional<FlatDiscountCouponV2> findFlatCouponById(Long couponId);

    CouponV2 save(CouponV2 couponV2);
}
