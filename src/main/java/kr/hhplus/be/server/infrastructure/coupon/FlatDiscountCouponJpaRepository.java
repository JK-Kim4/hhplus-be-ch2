package kr.hhplus.be.server.infrastructure.coupon;

import kr.hhplus.be.server.domain.coupon.FlatDiscountCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlatDiscountCouponJpaRepository extends JpaRepository<FlatDiscountCoupon, Long> {

    FlatDiscountCoupon findByCouponId(Long id);
}
