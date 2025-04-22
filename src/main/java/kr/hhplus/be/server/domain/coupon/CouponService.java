package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.NoResultException;
import kr.hhplus.be.server.domain.coupon.userCoupon.UserCoupon;
import kr.hhplus.be.server.domain.coupon.userCoupon.UserCouponRepository;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class CouponService {

    private CouponRepository couponRepository;
    private UserCouponRepository userCouponRepository;
    private UserRepository userRepository;

    public CouponService(
            CouponRepository couponRepository,
            UserCouponRepository userCouponRepository,
            UserRepository userRepository) {
        this.couponRepository = couponRepository;
        this.userCouponRepository = userCouponRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public UserCoupon saveUserCoupon(UserCoupon userCoupon) {
        return userCouponRepository.save(userCoupon);
    }

    @Transactional
    public UserCoupon issue(Long couponId, Long userId) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(NoResultException::new);
        User user = userRepository.findById(userId)
                .orElseThrow(NoResultException::new);

        UserCoupon userCoupon = coupon.issue(user, LocalDate.now());

        deductCouponQuantity(couponId); //-> worker

        return userCoupon;
    }

    @Transactional
    public void deductCouponQuantity(Long couponId) {
        Coupon coupon = couponRepository.findByIdWithPessimisticLock(couponId).orElseThrow(NoResultException::new);
        coupon.decreaseQuantity();
    }

    public void applyCouponToOrder(UserCoupon userCoupon, Order order) {
        if (Objects.isNull(userCoupon)) {
            return;
        }
        userCoupon.isUsable(LocalDate.now(), order.getUser());
        Integer finalPaymentPrice = userCoupon.discount(order.getTotalPrice());
        order.applyDiscount(userCoupon.getId(), finalPaymentPrice);
        userCoupon.updateUsedCouponInformation(order);
    }

    public Coupon findById(Long couponId) {
        return couponRepository.findById(couponId)
                .orElseThrow(NoResultException::new);
    }

    public boolean isAlreadyIssuedCoupon(Long couponId, Long userId) {
        return userCouponRepository.isAlreadyIssuedCoupon(userId, couponId);
    }

    public UserCoupon findUserCouponById(Long userCouponId) {
        return userCouponRepository.findById(userCouponId)
                .orElse(null);
    }

    public List<UserCoupon> findByUserId(Long userId) {
        return userCouponRepository.findByUserId(userId);
    }
}
