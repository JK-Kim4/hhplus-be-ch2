package kr.hhplus.be.server.domain.coupon;

public class FlatDiscountCoupon extends Coupon {

    private Integer discountAmount;

    @Override
    boolean validate() {
        return false;
    }
}
