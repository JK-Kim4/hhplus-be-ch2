package kr.hhplus.be.server.common.event;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponIssueRequestedEvent implements Serializable {

    private Long couponId;
    private Long userId;

    public static CouponIssueRequestedEvent of(Long couponId, Long userId){
        return new CouponIssueRequestedEvent(couponId, userId);
    }

    @Builder
    private CouponIssueRequestedEvent(Long couponId, Long userId) {
        this.couponId = couponId;
        this.userId = userId;
    }

}
