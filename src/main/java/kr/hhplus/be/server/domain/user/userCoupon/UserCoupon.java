package kr.hhplus.be.server.domain.user.userCoupon;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCoupon {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    protected User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupoon_id")
    protected Coupon coupon;

    @Column(name = "is_used")
    protected boolean isUsed;

    @Column(name = "issue_date_time")
    protected LocalDateTime issueDateTime;


    public UserCoupon(User user, Coupon coupon) {
        createValidation(user, coupon);

        this.user = user;
        this.coupon = coupon;
        this.isUsed = false;
        this.issueDateTime = LocalDateTime.now();
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void createValidation(User user, Coupon coupon) {

        if(user == null || coupon == null) {
            throw new IllegalArgumentException("파라미터가 누락되었습니다.");
        }

        if(!coupon.isBeforeExpiredDate(LocalDate.now())) {
            throw new IllegalArgumentException("만료된 쿠폰입니다.");
        }

        if(!coupon.hasEnoughQuantity()){
            throw new IllegalArgumentException("재고가 소진되었습니다.");
        }
    }
    public void isUsable(LocalDate targetDateTime, Long userId) {

        if(!coupon.isBeforeExpiredDate(targetDateTime)){
            throw new IllegalArgumentException("만료된 쿠폰입니다.");
        }

        if(!isCouponOwner(userId)){
            throw new IllegalArgumentException("사용할 수 없는 쿠폰입니다.");
        }

        if(isUsed){
            throw new IllegalArgumentException("이미 사용된 쿠폰입니다.");
        }
    }

    public boolean isCouponOwner(Long userId) {
        return userId.equals(this.user.getId());
    }

    public Integer discount(Integer price) {
        return coupon.calculateDiscount(price);
    }

    public void updateUsedFlag(boolean value) {
        this.isUsed = value;
    }
}
