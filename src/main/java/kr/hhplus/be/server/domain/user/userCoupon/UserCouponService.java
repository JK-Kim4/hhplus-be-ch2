package kr.hhplus.be.server.domain.user.userCoupon;

import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserCouponService {

    private final UserCouponRepository userCouponRepository;

    public UserCouponService(
            UserCouponRepository userCouponRepository) {
        this.userCouponRepository = userCouponRepository;
    }

    @Transactional(readOnly = true)
    public List<UserCoupon> findByUserId(Long userId) {
        return userCouponRepository.findByUserId(userId);
    }

    public UserCoupon findUserCouponById(Long userCouponId) {
        return userCouponRepository.findById(userCouponId)
                .orElseThrow(NoResultException::new);
    }

    public void save(UserCoupon userCoupon) {
        userCouponRepository.save(userCoupon);
    }
}
