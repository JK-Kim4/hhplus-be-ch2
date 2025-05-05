package kr.hhplus.be.server.application.coupon;

import jakarta.persistence.NoResultException;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.userCoupon.UserCoupon;
import kr.hhplus.be.server.domain.coupon.userCoupon.UserCouponInfo;
import kr.hhplus.be.server.domain.coupon.userCoupon.UserCouponRepository;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.interfaces.common.annotation.DistributedLock;
import kr.hhplus.be.server.interfaces.common.lock.LockExecutorType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class CouponCommandService {


    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;
    private final UserRepository userRepository;
    private final CouponQueryService couponQueryService;

    public CouponCommandService(
            CouponRepository couponRepository,
            UserCouponRepository userCouponRepository,
            UserRepository userRepository,
            CouponQueryService couponQueryService) {
        this.couponRepository = couponRepository;
        this.userCouponRepository = userCouponRepository;
        this.userRepository = userRepository;
        this.couponQueryService = couponQueryService;
    }

    //데이터베이스 락 미적용 메소드
    public UserCouponInfo.Issue issueV1(CouponCommand.Issue command) {
        Coupon coupon = couponRepository.findById(command.getCouponId())
                .orElseThrow(NoResultException::new);
        User user = userRepository.findById(command.getUserId())
                .orElseThrow(NoResultException::new);

        couponQueryService.findByCouponIdAndUserId(command.getCouponId(), command.getUserId())
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

    @DistributedLock(prefix = "couponId", key = "#command.couponId")
    public UserCouponInfo.Issue issueWithDistributeLock(CouponCommand.Issue command) {

        Coupon coupon = couponRepository.findById(command.getCouponId())
                .orElseThrow(NoResultException::new);
        User user = userRepository.findById(command.getUserId())
                .orElseThrow(NoResultException::new);

        couponQueryService.findByCouponIdAndUserId(command.getCouponId(), command.getUserId())
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
            key = "#command.couponId",
            leaseTime = 10,
            waitTime = 3,
            executor = LockExecutorType.SPIN)
    public UserCouponInfo.Issue issueWithSpinLock(CouponCommand.Issue command) {

        Coupon coupon = couponRepository.findById(command.getCouponId()).orElseThrow(NoResultException::new);
        User user = userRepository.findById(command.getUserId()).orElseThrow(NoResultException::new);

        couponQueryService.findByCouponIdAndUserId(command.getCouponId(), command.getUserId())
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

    public UserCouponInfo.Issue issue(CouponCommand.Issue command) {
        Coupon coupon = couponRepository.findByIdWithPessimisticLock(command.getCouponId())
                .orElseThrow(NoResultException::new);
        User user = userRepository.findById(command.getUserId())
                .orElseThrow(NoResultException::new);

        couponQueryService.findByCouponIdAndUserId(command.getCouponId(), command.getUserId())
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

}
