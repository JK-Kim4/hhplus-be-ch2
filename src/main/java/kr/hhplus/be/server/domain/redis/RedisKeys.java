package kr.hhplus.be.server.domain.redis;

public enum RedisKeys {

    DAILY_SALES_REPORT("products:dailySalesReport:%s"),
    COUPON_ISSUABLE_FLAG("coupon:issuable:%s"),
    COUPON_REQUEST_ISSUE("coupon:request:%s"),
    COUPON_META("coupon:meta"),
    COUPON_ACTIVE("coupon:active");

    private final String pattern;

    RedisKeys(String pattern) {
        this.pattern = pattern;
    }

    public String format(Object... args) {
        return String.format(pattern, args);
    }

    public static String[] getCouponKeys(Long couponId){
        return new String[]{
                COUPON_ISSUABLE_FLAG.format(couponId),
                COUPON_REQUEST_ISSUE.format(couponId),
                COUPON_META.name(),
                COUPON_ACTIVE.name()};
    }


}
