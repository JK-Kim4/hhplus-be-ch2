package kr.hhplus.be.server.domain.coupon;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter @Builder
public class CouponTemplate {

    private Long id;
    private String name;
    private CouponType couponType;
    private Integer remainingQuantity;
    private LocalDateTime expireDateTime;

}
