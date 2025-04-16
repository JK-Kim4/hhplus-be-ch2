package kr.hhplus.be.server.domain.user;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.user.userCoupon.UserCoupon;
import kr.hhplus.be.server.domain.user.userCoupon.UserCoupons;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity(name = "user")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER)
    protected Point point;

    @Embedded
    private UserCoupons userCoupons;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User(String name) {

        this.name = name;
        userCoupons = new UserCoupons();
    }

    public User(Long id, String name){
        this.id = id;
        this.name = name;
        userCoupons = new UserCoupons();
    }

    public boolean isAlreadyIssuedCoupon(Coupon coupon) {
        return userCoupons.isAlreadyIssuedCoupon(coupon);
    }

    public Integer point(){
        return point.getAmount();
    }

    public void addUserCoupon(UserCoupon userCoupon) {
        this.userCoupons.addUserCoupon(userCoupon);
    }

    public void chargePoint(Integer amount) {
        this.point.charge(amount);
    }

    public void deductPoint(Integer amount) {
        this.point.deduct(amount);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
