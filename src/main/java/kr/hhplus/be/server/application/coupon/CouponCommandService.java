package kr.hhplus.be.server.application.coupon;

import jakarta.persistence.NoResultException;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.userCoupon.UserCoupon;
import kr.hhplus.be.server.domain.coupon.userCoupon.UserCouponInfo;
import kr.hhplus.be.server.domain.coupon.userCoupon.UserCouponRepository;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.infrastructure.coupon.InMemoryCouponIssueQueue;
import kr.hhplus.be.server.interfaces.common.annotation.DistributedLock;
import kr.hhplus.be.server.interfaces.common.lock.LockExecutorType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Objects;

@Service
@Transactional
public class CouponCommandService {


    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;
    private final UserRepository userRepository;
    private final InMemoryCouponIssueQueue inMemoryCouponIssueQueue;
    private final CouponQueryService couponQueryService;

    public CouponCommandService(
            CouponRepository couponRepository,
            UserCouponRepository userCouponRepository,
            UserRepository userRepository,
            InMemoryCouponIssueQueue inMemoryCouponIssueQueue,
            CouponQueryService couponQueryService) {
        this.couponRepository = couponRepository;
        this.userCouponRepository = userCouponRepository;
        this.userRepository = userRepository;
        this.inMemoryCouponIssueQueue = inMemoryCouponIssueQueue;
        this.couponQueryService = couponQueryService;
    }

    //데이터베이스 락 미적용 메소드
    public UserCouponInfo.Issue issueV1(Long couponId, Long userId) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(NoResultException::new);
        User user = userRepository.findById(userId)
                .orElseThrow(NoResultException::new);

        couponQueryService.findByCouponIdAndUserId(couponId, userId)
                .ifPresent(uc -> {throw new IllegalArgumentException("이미 발급된 쿠폰입니다.");});

        UserCoupon userCoupon = coupon.issue(user, LocalDate.now());

        userCouponRepository.save(userCoupon);

        return UserCouponInfo.Issue.of(
                userCoupon.getId(),
                userCoupon.getCoupon().getId(),
                userCoupon.getUser().getId(),
                userCoupon.getIssueDateTime()
        );
    }

    @DistributedLock(prefix = "couponId", key = "#couponId")
    public UserCouponInfo.Issue issueWithDistributeLock(Long couponId, Long userId) {

        Coupon coupon = couponRepository.findById(couponId).orElseThrow(NoResultException::new);
        User user = userRepository.findById(userId).orElseThrow(NoResultException::new);

        couponQueryService.findByCouponIdAndUserId(couponId, userId)
                .ifPresent(uc -> {throw new IllegalArgumentException("이미 발급된 쿠폰입니다.");});

        UserCoupon userCoupon = coupon.issue(user, LocalDate.now());

        couponRepository.save(coupon);
        userCouponRepository.save(userCoupon);

        return UserCouponInfo.Issue.of(
                userCoupon.getId(),
                userCoupon.getCoupon().getId(),
                userCoupon.getUser().getId(),
                userCoupon.getIssueDateTime()
        );
    }

    @DistributedLock(
            prefix = "couponId",
            key = "#couponId",
            leaseTime = 10,
            waitTime = 3,
            executor = LockExecutorType.SPIN)
    public UserCouponInfo.Issue issueWithSpinLock(Long couponId, Long userId) {

        Coupon coupon = couponRepository.findById(couponId).orElseThrow(NoResultException::new);
        User user = userRepository.findById(userId).orElseThrow(NoResultException::new);

        couponQueryService.findByCouponIdAndUserId(couponId, userId)
                .ifPresent(uc -> {throw new IllegalArgumentException("이미 발급된 쿠폰입니다.");});

        UserCoupon userCoupon = coupon.issue(user, LocalDate.now());

        userCouponRepository.save(userCoupon);

        return UserCouponInfo.Issue.of(
                userCoupon.getId(),
                userCoupon.getCoupon().getId(),
                userCoupon.getUser().getId(),
                userCoupon.getIssueDateTime()
        );
    }

    public UserCouponInfo.Issue issue(Long couponId, Long userId) {
        Coupon coupon = couponRepository.findByIdWithPessimisticLock(couponId)
                .orElseThrow(NoResultException::new);
        User user = userRepository.findById(userId)
                .orElseThrow(NoResultException::new);

        couponQueryService.findByCouponIdAndUserId(couponId, userId)
                .ifPresent(uc -> {throw new IllegalArgumentException("이미 발급된 쿠폰입니다.");});

        UserCoupon userCoupon = coupon.issue(user, LocalDate.now());

        userCouponRepository.save(userCoupon);

        return UserCouponInfo.Issue.of(
                userCoupon.getId(),
                userCoupon.getCoupon().getId(),
                userCoupon.getUser().getId(),
                userCoupon.getIssueDateTime()
        );
    }

    public void deductCouponQuantity(Long couponId) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(NoResultException::new);
        coupon.decreaseQuantity();
    }

    public UserCoupon applyCouponToOrder(UserCoupon userCoupon, Order order) {
        if (Objects.isNull(userCoupon)) {
            return null;
        }
        userCoupon.isUsable(LocalDate.now(), order.getUser());
        Integer finalPaymentPrice = userCoupon.discount(order.getTotalPrice());
        order.applyDiscount(userCoupon.getId(), finalPaymentPrice);
        userCoupon.updateUsedCouponInformation(order);
        return userCoupon;
    }

}
