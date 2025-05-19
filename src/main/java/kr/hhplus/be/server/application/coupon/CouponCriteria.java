package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.domain.coupon.CouponCommand;
import lombok.Builder;
import lombok.Getter;

public class CouponCriteria {

    @Getter
    public static class Issue{
        Long couponId;
        Long userId;

        public static Issue of(Long couponId, Long userId){
            return Issue.builder().couponId(couponId).userId(userId).build();
        }

        @Builder
        private Issue(Long couponId, Long userId) {
            this.couponId = couponId;
            this.userId = userId;
        }

        public CouponCommand.Issue toCommand() {
            return CouponCommand.Issue.builder()
                    .couponId(couponId)
                    .userId(userId)
                    .build();
        }
    }

    @Getter
    public static class RequestRegister {
        Long couponId;
        Long userId;

        @Builder
        private RequestRegister(Long couponId, Long userId) {
            this.couponId = couponId;
            this.userId = userId;
        }

        public CouponCommand.Issue toCommand() {
            return CouponCommand.Issue.builder()
                    .couponId(couponId)
                    .userId(userId)
                    .build();
        }

        public CouponCommand.RegisterApplicant toRegisterCommand() {
            return CouponCommand.RegisterApplicant.of(couponId, userId);
        }
    }
}
