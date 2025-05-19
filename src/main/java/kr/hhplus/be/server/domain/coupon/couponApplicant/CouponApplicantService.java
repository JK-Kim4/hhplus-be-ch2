package kr.hhplus.be.server.domain.coupon.couponApplicant;

import kr.hhplus.be.server.domain.coupon.CouponCommand;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class CouponApplicantService {

    private final CouponApplicantInMemoryRepository couponApplicantInMemoryRepository;

    public CouponApplicantService(CouponApplicantInMemoryRepository couponApplicantInMemoryRepository) {
        this.couponApplicantInMemoryRepository = couponApplicantInMemoryRepository;;
    }

    public void existIssuableCoupon(Long couponId) {
        if(!couponApplicantInMemoryRepository.existIssuableCoupon(couponId)){
            throw new IllegalArgumentException("발급 가능한 쿠폰이 존재하지않습니다.");
        }
    }

    public void isAlreadyRequestUser(Long couponId, Long userId) {
        if(couponApplicantInMemoryRepository.findByCouponIdAndUserId(couponId, userId)
                .isPresent()){
            throw new IllegalArgumentException("발급 요청 처리중입니다.");
        }
    }

    public void isAlreadyWinner(Long couponId, Long userId){
        if(couponApplicantInMemoryRepository.findWinnerByCouponIdAndUserId(couponId, userId)
                .isPresent()){
            throw new IllegalArgumentException("이미 당첨된 쿠폰입니다.");
        }
    }

    public CouponApplicantInfo.RegisterApplicant registerCouponApplicant(CouponCommand.RegisterApplicant command){
        LocalDateTime dateTime = LocalDateTime.now();
        long millis = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        couponApplicantInMemoryRepository.registerCouponApplicant(command.getCouponId(), command.getUserId(), millis);

        return CouponApplicantInfo.RegisterApplicant.of(command.getCouponId(), command.getUserId(), dateTime);
    }

    public CouponApplicantInfo.Applicants fetchApplicantUserIds(Long couponId) {
        List<CouponApplicant> byCouponId = couponApplicantInMemoryRepository.findByCouponId(couponId);
        return CouponApplicantInfo.Applicants.of(byCouponId.stream().map(CouponApplicant::getUserId).toList());
    }

    public void validationRegister(CouponCommand.Issue command) {
        existIssuableCoupon(command.getCouponId());

        isAlreadyRequestUser(command.getCouponId(), command.getUserId());

        isAlreadyWinner(command.getCouponId(), command.getUserId());
    }

    public CouponApplicantInfo.IssuableCoupons findIssuableCouponIds() {
        return CouponApplicantInfo.IssuableCoupons.of(couponApplicantInMemoryRepository.findIssuableCouponIds());
    }

    public void deleteExpiredCouponKeys(CouponCommand.DeleteKey command) {
        deleteIssuableCouponKey(command);

        deleteCouponApplicantKey(command);
    }

    public void deleteIssuableCouponKey(CouponCommand.DeleteKey command) {
        couponApplicantInMemoryRepository.deleteIssuableCouponKey(command.getCouponId());
    }

    public void deleteCouponApplicantKey(CouponCommand.DeleteKey command) {
        couponApplicantInMemoryRepository.deleteCouponApplicantKey(command.getCouponId());
    }
}
