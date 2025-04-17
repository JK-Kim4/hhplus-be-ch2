package kr.hhplus.be.server.infrastructure.coupon;

import kr.hhplus.be.server.domain.couponv2.FlatDiscountCouponV2;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlatDiscountCouponJpaRepository extends JpaRepository<FlatDiscountCouponV2, Long> {

    FlatDiscountCouponV2 findByCouponId(Long id);
}
