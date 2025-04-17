package kr.hhplus.be.server.infrastructure.coupon;

import kr.hhplus.be.server.domain.couponv2.CouponV2;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponV2JpaRepository extends JpaRepository<CouponV2, Long> {
}
