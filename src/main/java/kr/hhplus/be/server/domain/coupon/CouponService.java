package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.NoResultException;
import kr.hhplus.be.server.domain.salesStat.TypedScore;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CouponService {

    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;

    public CouponService(
            CouponRepository couponRepository,
            UserCouponRepository userCouponRepository) {
        this.couponRepository = couponRepository;
        this.userCouponRepository = userCouponRepository;
    }

    public CouponInfo.Coupon findById(Long couponId){
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(NoResultException::new);

        return CouponInfo.Coupon.from(coupon);
    }

    public CouponInfo.Coupon findByIdWithPessimisticLock(Long couponId){
        Coupon coupon = couponRepository.findByIdWithPessimisticLock(couponId)
                .orElseThrow(NoResultException::new);

        return CouponInfo.Coupon.from(coupon);
    }

    public CouponInfo.UserCouponOptional findUserCouponByCouponIdAndUserId(Long couponId, Long userId){
        return CouponInfo.UserCouponOptional.from(userCouponRepository.findByCouponIdAndUserId(couponId, userId));
    }

    public CouponInfo.Issue issueUserCoupon(CouponCommand.Issue command){
        Coupon coupon = couponRepository.findById(command.couponId)
                .orElseThrow(NoResultException::new);

        UserCoupon userCoupon = UserCoupon.issue(coupon, command.getUserId());
        userCouponRepository.save(userCoupon);

        return CouponInfo.Issue.from(userCoupon);
    }

    public CouponInfo.Coupons findCouponsByUserId(Long userId) {
        List<UserCoupon> userCoupons = userCouponRepository.findByUserId(userId);
        return CouponInfo.Coupons.from(userCoupons.stream().map(UserCoupon::getCoupon).toList());
    }

    public Integer getAvailableQuantityByCouponId(Long couponId) {
        return couponRepository.getAvailableQuantityByCouponId(couponId);
    }

    public void issueCouponsToApplicants(Long couponId, List<TypedScore> requestUserSetByRedisKey) {
        requestUserSetByRedisKey.forEach(typedScore ->
                    this.issueUserCoupon(
                        CouponCommand.Issue.of(couponId, Long.valueOf(typedScore.member())))
        );
    }

    public void issueCouponToApplicantsV2(Long couponId, List<Long> applicantIds) {
        applicantIds.forEach(userId -> {
            this.issueUserCoupon(CouponCommand.Issue.of(couponId, userId));
        });
    }

    public CouponInfo.AvailableCouponIds getIssuableCouponIds() {

        List<Coupon> coupons = couponRepository.findIssuableCoupons(LocalDate.now());

        return CouponInfo.AvailableCouponIds.of(coupons.stream()
                .map(Coupon::getId)
                .toList());
    }

    public CouponInfo.ExpiredCouponIds getExpiredCouponIds(){
        List<Coupon> expiredCoupons = couponRepository.findExpiredCoupons(LocalDate.now());

        return CouponInfo.ExpiredCouponIds.of(expiredCoupons.stream()
                .map(Coupon::getId)
                .toList());
    }

    public CouponInfo.AvailableCouponIds getCouponIdsWithRemainingQuantity() {
        List<Coupon> coupons = couponRepository.getCouponsWithRemainingQuantity();

        return CouponInfo.AvailableCouponIds.of(coupons.stream()
                .map(Coupon::getId)
                .toList());
    }
}
