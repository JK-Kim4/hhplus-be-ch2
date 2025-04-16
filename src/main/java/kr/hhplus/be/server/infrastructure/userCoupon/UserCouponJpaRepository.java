package kr.hhplus.be.server.infrastructure.userCoupon;

import kr.hhplus.be.server.domain.user.userCoupon.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCouponJpaRepository extends JpaRepository<UserCoupon, Long> {

    List<UserCoupon> findByUserId(Long userId);
}
