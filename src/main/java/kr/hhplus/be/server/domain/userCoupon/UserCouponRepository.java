package kr.hhplus.be.server.domain.userCoupon;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserCouponRepository {

    UserCoupon save(UserCoupon userCoupon);

    List<UserCoupon> findByUserId(Long userId);

    Optional<UserCoupon> findById(Long userCouponId);
}
