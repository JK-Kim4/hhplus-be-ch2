package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.userCoupon.UserCoupon;
import kr.hhplus.be.server.interfaces.exception.InvalidAmountException;

import java.time.LocalDateTime;

import static java.lang.Math.max;

public class FlatDiscountCoupon extends Coupon {

    private Integer discountAmount;

    @Override
    boolean validate(LocalDateTime targetDateTime) {
        return targetDateTime.isBefore(this.getExpireDateTime()) && this.getRemainingQuantity().get() > 0;
    }

    @Override
    public UserCoupon issue(User user) {

        while (true) {
            int currentStock = getRemainingQuantity().get();
            if (currentStock <= 0) {
                System.out.println(user + " - 품절");
                throw new InvalidAmountException("이미 품절된 상품입니다.");
            }

            // 재고를 원자적으로 1 줄임
            if (getRemainingQuantity().compareAndSet(currentStock, currentStock - 1)) {
                System.out.println(user + " - 구매 성공! 남은 재고: " + getRemainingQuantity().get());
                return new UserCoupon(user, (FlatDiscountCoupon) this);
            }
            // 실패하면 다시 시도 (다른 스레드가 먼저 차감한 경우)
        }

    }

    @Override
    public Integer discount(Integer price) {
        return max(0, price - discountAmount) ;
    }

    public FlatDiscountCoupon(){super();}

    public FlatDiscountCoupon(CouponTemplate template){super(template);}

    public FlatDiscountCoupon(CouponTemplate template, Integer discountAmount){
        super(template);
        this.discountAmount = discountAmount;
    }

    public Integer getDiscountAmount() {
        return discountAmount;
    }
}
