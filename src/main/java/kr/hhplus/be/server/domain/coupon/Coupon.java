package kr.hhplus.be.server.domain.coupon;

import java.time.LocalDateTime;

public abstract class Coupon {

    private Long id;
    private String name;
    private CouponType couponType;
    private Integer remainingQuantity;
    private LocalDateTime expireDateTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    abstract boolean validate();

}
