package kr.hhplus.be.server.domain.userCoupon;

import jakarta.persistence.NoResultException;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserCouponService {

    private UserCouponRepository userCouponRepository;
    private CouponRepository couponRepository;
    private UserRepository userRepository;

    public UserCouponService(
            UserCouponRepository userCouponRepository,
            CouponRepository couponRepository,
            UserRepository userRepository) {
        this.userCouponRepository = userCouponRepository;
        this.couponRepository = couponRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = false)
    public UserCouponIssueCommand.Response issue(UserCouponIssueCommand command) {
        Coupon coupon = couponRepository.findById(command.getCouponId()).orElseThrow(NoResultException::new);
        User user = userRepository.findById(command.getUserId()).orElseThrow(NoResultException::new);

        return new UserCouponIssueCommand.Response(userCouponRepository.save(coupon.issue(user)));
    }

    @Transactional(readOnly = true)
    public List<UserCoupon> findByUserId(Long userId) {
        return userCouponRepository.findByUserId(userId);
    }
}
