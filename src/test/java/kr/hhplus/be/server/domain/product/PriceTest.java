package kr.hhplus.be.server.domain.product;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PriceTest {

    @Test
    void 금액이_음수이면_예외를_던진다() {
        assertThatThrownBy(() -> Price.of(BigDecimal.valueOf(-100)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("가격은 NUll이거나 음수일 수 없습니다.");
    }

    @Test
    void 금액을_정상적으로_생성할_수_있다() {
        Price price = Price.of(BigDecimal.valueOf(1000));
        assertThat(price.getAmount()).isEqualByComparingTo("1000");
    }

    @Test
    void 금액_덧셈_연산이_가능하다() {
        Price a = Price.of(BigDecimal.valueOf(1000));
        Price b = Price.of(BigDecimal.valueOf(500));
        Price result = a.add(b);
        assertThat(result.getAmount()).isEqualByComparingTo("1500");
    }

    @Test
    void 금액_뺄셈_연산이_가능하다() {
        Price a = Price.of(BigDecimal.valueOf(1000));
        Price b = Price.of(BigDecimal.valueOf(400));
        Price result = a.subtract(b);
        assertThat(result.getAmount()).isEqualByComparingTo("600");
    }

    @Test
    void 금액_수정이_가능하다(){
        Price a = Price.of(BigDecimal.valueOf(1000));
        Price b = Price.of(BigDecimal.valueOf(400));
        Price result = a.update(b);
        assertThat(result.getAmount()).isEqualByComparingTo("400");
    }

    @Test
    void 금액_비교가_가능하다() {
        Price a = Price.of(BigDecimal.valueOf(1000));
        Price b = Price.of(BigDecimal.valueOf(500));
        assertThat(a.isGreaterThan(b)).isTrue();
    }
}
