package kr.hhplus.be.server.domain.coupon;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class DiscountPolicyTest {

    @Test
    void 정액_할인_정책은_정해진_금액을_반환한다() {
        BigDecimal result = DiscountPolicy.FLAT.calculate(BigDecimal.valueOf(10000), BigDecimal.valueOf(3000));
        assertThat(result).isEqualByComparingTo("3000");
    }

    @Test
    void 정률_할인_정책은_비율에_따른_할인금액을_반환한다() {
        BigDecimal result = DiscountPolicy.RATE.calculate(BigDecimal.valueOf(10000), BigDecimal.valueOf(15));
        assertThat(result).isEqualByComparingTo("1500");
    }
}
