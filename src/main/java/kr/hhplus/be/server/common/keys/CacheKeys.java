package kr.hhplus.be.server.common.keys;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public enum CacheKeys {

    DAILY_SALES_REPORT("products:dailySalesReport:%s", true),
    COUPON_ISSUABLE_FLAG("coupon:issuable:%s", true),
    COUPON_ISSUABLE_ID_SET("coupon:issuable", false),
    COUPON_REQUEST_ISSUE("coupon:request:%s", true),
    COUPON_WINNER("coupon:winner:%s", true),
    COUPON_META("coupon:meta", false),
    COUPON_ACTIVE("coupon:active", false),
    EXCEPTION_HISTORY("exception:history:%s", true),
    IDEMPOTENCY_PAYMENT("idempotency:payment:%s", true),
    FAILED_RETRY_LOG("failed:retryLog", false),;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final String pattern;
    private final boolean supportsFormat;

    CacheKeys(String pattern, boolean supportsFormat) {
        this.pattern = pattern;
        this.supportsFormat = supportsFormat;
    }

    public String format(Object arg) {
        if (!supportsFormat) {
            throw new UnsupportedOperationException(name() + "에서는 format을 사용할 수 없습니다.");
        }

        if (arg instanceof LocalDate date) {
            return String.format(pattern, DATE_FORMATTER.format(date));
        }

        return String.format(pattern, arg);
    }
}
