package kr.hhplus.be.server.domain.coupon;

public class RateDiscountCoupon extends Coupon {

    private Integer discountRate;

    @Override
    boolean validate() {
        return false;
    }

}
