package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    public Integer applyCoupon(Order order) {
        //할인 금액 계산
        Integer resultPrice = coupon.calculateDiscount(order.getTotalPrice());

        //적용 주문 정보 입력
        this.appliedOrder = order;
        this.applyDateTime = LocalDateTime.now();

        return resultPrice;
    }

    public UserCoupon(User user, Coupon coupon) {

        this.user = user;
        this.coupon = coupon;
        this.issueDateTime = LocalDateTime.now();

    }
}
