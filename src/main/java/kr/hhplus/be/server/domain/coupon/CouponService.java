package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.NoResultException;
import kr.hhplus.be.server.domain.coupon.userCoupon.UserCoupon;
import kr.hhplus.be.server.domain.coupon.userCoupon.UserCouponRepository;
import kr.hhplus.be.server.domain.order.Order;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class CouponService {

    private CouponRepository couponRepository;
    private UserCouponRepository userCouponRepository;

    public CouponService(
            CouponRepository couponRepository,
            UserCouponRepository userCouponRepository) {
        this.couponRepository = couponRepository;
        this.userCouponRepository = userCouponRepository;
    }

    public UserCoupon saveUserCoupon(UserCoupon userCoupon) {
        return userCouponRepository.save(userCoupon);
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

    public boolean isAlreadyIssuedCoupon(Long userId, Long couponId) {
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
