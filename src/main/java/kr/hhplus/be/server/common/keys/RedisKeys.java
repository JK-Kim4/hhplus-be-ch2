package kr.hhplus.be.server.common.keys;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public enum RedisKeys {

    DAILY_SALES_REPORT("products:dailySalesReport:%s", true),
    COUPON_ISSUABLE_FLAG("coupon:issuable:%s", true),
    COUPON_ISSUABLE_ID_SET("coupon:issuable", false),
    COUPON_REQUEST_ISSUE("coupon:request:%s", true),
    COUPON_WINNER("coupon:winner:%s", true),
    COUPON_META("coupon:meta", false),
    COUPON_ACTIVE("coupon:active", false),
    EXCEPTION_HISTORY("exception:history:%s", true);

    private final String pattern;
    private final boolean formattable;


    public static final DateTimeFormatter LOCAL_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    RedisKeys(String pattern, boolean formattable) {
        this.pattern = pattern;
        this.formattable = formattable;
    }

    public String format(Object arg) {

        if (!formattable) {
            throw new UnsupportedOperationException(this.name() + "에서는 format을 사용할 수 없습니다.");
        }

        if (arg instanceof LocalDate) {
            return String.format(pattern, LOCAL_DATE_FORMAT.format((LocalDate) arg));
        }

        return String.format(pattern, arg);
    }

    public static String[] getCouponKeys(Long couponId){
        return new String[]{
                COUPON_ISSUABLE_FLAG.format(couponId),
                COUPON_REQUEST_ISSUE.format(couponId),
                COUPON_META.name(),
                COUPON_ACTIVE.name()};
    }
}
