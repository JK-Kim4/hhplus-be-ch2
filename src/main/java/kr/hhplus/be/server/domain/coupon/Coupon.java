package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.userCoupon.UserCoupon;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Coupon {

    private Long id;
    private String name;
    private CouponType couponType;
    private AtomicInteger remainingQuantity;
    private LocalDateTime expireDateTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    abstract boolean validate(LocalDateTime targetDateTime);
    public abstract UserCoupon issue(User user);
    public abstract Integer discount(Integer integer);

    public Coupon(CouponTemplate couponTemplate) {
        this.id = couponTemplate.getId();
        this.name = couponTemplate.getName();
        this.couponType = couponTemplate.getCouponType();
        this.remainingQuantity = couponTemplate.getRemainingQuantity();
        this.expireDateTime = couponTemplate.getExpireDateTime();
    }

    public Coupon(Long id, String name, CouponType couponType, AtomicInteger remainingQuantity, LocalDateTime expireDateTime, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.couponType = couponType;
        this.remainingQuantity = remainingQuantity;
        this.expireDateTime = expireDateTime;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public boolean isBeforeExpiredDate(LocalDateTime targetDateTime) {
        return expireDateTime.isBefore(targetDateTime);
    }
}
