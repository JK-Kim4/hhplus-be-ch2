package kr.hhplus.be.server.domain.coupon;

import java.math.BigDecimal;

public enum DiscountPolicy {
    FLAT {
        @Override
        public BigDecimal calculate(BigDecimal orderAmount, BigDecimal discountAmount) {
            return discountAmount;
        }
    },
    RATE {
        @Override
        public BigDecimal calculate(BigDecimal orderAmount, BigDecimal discountRate) {
            return orderAmount
                    .multiply(discountRate)
                    .divide(BigDecimal.valueOf(100));
        }
    };

    public abstract BigDecimal calculate(BigDecimal orderAmount, BigDecimal discountValue);
}
