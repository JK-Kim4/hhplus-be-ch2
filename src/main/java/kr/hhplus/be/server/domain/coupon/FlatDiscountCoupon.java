package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.userCoupon.UserCoupon;
import kr.hhplus.be.server.interfaces.exception.InvalidAmountException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static java.lang.Math.max;

@Entity
@DiscriminatorValue("FLAT")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FlatDiscountCoupon extends Coupon {

    private Integer discountAmount;

    public FlatDiscountCoupon(CouponTemplate template){super(template);}

    public FlatDiscountCoupon(CouponTemplate template, Integer discountAmount){
        super(template);
        this.discountAmount = discountAmount;
    }

    @Override
    boolean validate(LocalDateTime targetDateTime) {
        return targetDateTime.isBefore(this.getExpireDateTime()) && this.getRemainingQuantity() > 0;
    }

    @Override
    public UserCoupon issue(User user) {
        int currentStock = getRemainingQuantity();

        if (currentStock <= 0) {
            System.out.println(user + " - 품절");
            throw new InvalidAmountException("이미 품절된 상품입니다.");
        }

        decreaseRemainingQuantity();
        System.out.println(user + " - 구매 성공! 남은 재고: " + getRemainingQuantity());
        return new UserCoupon(user, this);

    }

    @Override
    public Integer discount(Integer price) {
        return max(0, price - discountAmount) ;
    }

    public Integer getDiscountAmount() {
        return discountAmount;
    }
}
