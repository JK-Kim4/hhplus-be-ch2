package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.userCoupon.UserCoupon;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

//TODO Composition
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "c_type")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Coupon {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    private CouponType couponType;

    @Column(name = "remaining_quantity")
    private Integer remainingQuantity;

    @Column
    private LocalDateTime expireDateTime;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    abstract boolean validate(LocalDateTime targetDateTime);
    public abstract UserCoupon issue(User user);
    public abstract Integer discount(Integer integer);

    public Coupon(CouponTemplate couponTemplate) {
        if(couponTemplate.getId() == null) {
            this.id = couponTemplate.getId();
        }

        this.name = couponTemplate.getName();
        this.couponType = couponTemplate.getCouponType();
        this.remainingQuantity = couponTemplate.getRemainingQuantity();
        this.expireDateTime = couponTemplate.getExpireDateTime();
    }

    public boolean isBeforeExpiredDate(LocalDateTime targetDateTime) {
        return targetDateTime.isBefore(expireDateTime);
    }

    public boolean hasEnoughQuantity() {
        return remainingQuantity > 0;
    }

    public void decreaseRemainingQuantity() {
        remainingQuantity--;
    }
}
