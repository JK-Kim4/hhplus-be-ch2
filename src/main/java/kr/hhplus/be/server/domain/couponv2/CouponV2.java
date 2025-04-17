package kr.hhplus.be.server.domain.couponv2;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import static java.lang.Math.max;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponV2 {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "quantity")
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private CouponType couponType;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    protected FlatDiscountCouponV2 flatDiscountCoupon;

    @Column
    private LocalDate expireDate;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CouponV2 createFlatCoupon(String name, Integer quantity, LocalDate expireDate, Integer discountAmount){
        return new CouponV2(name, CouponType.FLAT, quantity, expireDate, discountAmount);
    }

    public CouponV2(String name, CouponType couponType, Integer quantity, LocalDate expireDate) {

        createValidation(name, couponType, quantity, expireDate, LocalDate.now());

        this.name = name;
        this.couponType = couponType;
        this.quantity = quantity;
        this.expireDate = expireDate;
    }

    private CouponV2 (String name, CouponType couponType, Integer quantity, LocalDate expireDate, Integer discountAmount) {

        createValidation(name, couponType, quantity, expireDate, LocalDate.now());

        this.name = name;
        this.couponType = CouponType.FLAT;
        this.flatDiscountCoupon = new FlatDiscountCouponV2(this, discountAmount);
        this.quantity = quantity;
        this.expireDate = expireDate;
    }

    public CouponV2(String name, CouponType couponType, Integer quantity, LocalDate expireDate, LocalDate nowDate) {

        createValidation(name, couponType, quantity, expireDate, nowDate);

        this.name = name;
        this.couponType = couponType;
        this.quantity = quantity;
        this.expireDate = expireDate;
    }

    public boolean isBeforeExpiredDate(LocalDate targetDate) {
        return targetDate.isBefore(expireDate);
    }

    public boolean hasEnoughQuantity() {
        return quantity > 0;
    }

    public void decreaseQuantity() {

        if(!hasEnoughQuantity()){
            throw new IllegalArgumentException("쿠폰 수량이 부족합니다.");
        }

        quantity--;
    }

    public Integer calculateDiscount(Integer totalPrice) {

        if(CouponType.FLAT.equals(couponType)){
            return calculateFlatDiscount(totalPrice);
        }

        throw new IllegalArgumentException("올바르지 않은 쿠폰입니다.");

    }

    public Integer calculateFlatDiscount(Integer totalPrice) {
        return max(0, totalPrice - flatDiscountCoupon.getDiscountAmount());
    }

    public UserCouponV2 issueUserCoupon(User user, LocalDate issuedDate) {
        validation(issuedDate);
        decreaseQuantity();
        return new UserCouponV2(user, this);
    }

    public void validation(LocalDate issuedDate){

        if(!hasEnoughQuantity()){
            throw new IllegalArgumentException("쿠폰 수량이 부족합니다.");
        }

        if(!isBeforeExpiredDate(issuedDate)){
            throw new IllegalArgumentException("만료된 쿠폰입니다.");
        }

    }

    private void createValidation(
            String name, CouponType couponType, Integer quantity, LocalDate expireDate, LocalDate nowDate) {

        if(Objects.isNull(name) || name.isBlank()){
            throw new IllegalArgumentException("쿠폰명을 입력해주세요.");
        }

        if(Objects.isNull(couponType)){
            throw new IllegalArgumentException("쿠폰 타입을 입력해주세요.");
        }

        if(Objects.isNull(quantity) || quantity <= 0){
            throw new IllegalArgumentException("올바른 쿠폰 수량을 입력해주세요. (최소 1개 이상)");
        }

        if (Objects.isNull(expireDate) || expireDate.isBefore(nowDate)) {
            throw new IllegalArgumentException("쿠폰 만료일은 현재 이전으로 설정할수없습니다.");
        }

    }
}
