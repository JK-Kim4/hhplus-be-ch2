package kr.hhplus.be.server.domain.userCoupon;

import jakarta.persistence.NoResultException;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserCouponService {

    private final OrderRepository orderRepository;
    private UserCouponRepository userCouponRepository;
    private CouponRepository couponRepository;
    private UserRepository userRepository;

    public UserCouponService(
            UserCouponRepository userCouponRepository,
            CouponRepository couponRepository,
            UserRepository userRepository,
            OrderRepository orderRepository) {
        this.userCouponRepository = userCouponRepository;
        this.couponRepository = couponRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
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

    public void applyDiscount(Long orderId, Long userCouponId) {
        UserCoupon userCoupon = userCouponRepository.findById(userCouponId)
                .orElseThrow(NoResultException::new);

        userCoupon.isUsable(LocalDateTime.now());

        Order order = orderRepository.findById(orderId)
                .orElseThrow(NoResultException::new);

        order.applyCoupon(userCoupon);
    }

    public UserCoupon findUserCouponById(Long userCouponId) {
        return userCouponRepository.findById(userCouponId)
                .orElseThrow(NoResultException::new);
    }
}
