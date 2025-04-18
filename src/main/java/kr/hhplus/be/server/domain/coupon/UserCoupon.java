package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    protected User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupoon_id")
    protected Coupon coupon;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order appliedOrder;

    @Column(name = "issue_date_time")
    protected LocalDateTime issueDateTime;

    @Column(name = "apply_date_time")
    protected LocalDateTime applyDateTime;

    public static UserCoupon create(User user, Coupon coupon) {
        if(Objects.isNull(user) || Objects.isNull(coupon)) {
            throw new IllegalArgumentException("필수 파라미터가 누락되었습니다.");
        }

        return new UserCoupon(user, coupon);
    }

    private UserCoupon(User user, Coupon coupon) {
        this.user = user;
        this.coupon = coupon;
        this.issueDateTime = LocalDateTime.now();
    }

    public void updateUsedCouponInformation(Order order) {
        this.appliedOrder = order;
        this.applyDateTime = LocalDateTime.now();
    }

    public boolean isCouponOwner(Long userId) {
        return userId.equals(this.user.getId());
    }

    public Integer discount(Integer price) {
        return coupon.calculateDiscount(price);
    }

    public void isUsable(LocalDate targetDateTime, Long userId) {

        if(!coupon.isBeforeExpiredDate(targetDateTime)){
            throw new IllegalArgumentException("만료된 쿠폰입니다.");
        }

        if(!isCouponOwner(userId)){
            throw new IllegalArgumentException("사용할 수 없는 쿠폰입니다.");
        }

    }
}
