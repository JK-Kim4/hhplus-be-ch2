package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.product.Price;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class PriceTest {

    @Test
    void 가격의_합연산() {
        Price price1 = Price.of(BigDecimal.valueOf(1000));
        Price price2 = Price.of(BigDecimal.valueOf(2000));

        Price result = price1.add(price2);

        assertEquals(BigDecimal.valueOf(3000), result.getAmount());
    }

    @Test
    void 가격의_곱연산() {
        Price price = Price.of(BigDecimal.valueOf(500));

        Price result = price.multiply(3);

        assertEquals(BigDecimal.valueOf(1500), result.getAmount());
    }

    @Test
    void 가격이_음수일경우_오류() {
        assertThrows(IllegalArgumentException.class, () -> Price.of(BigDecimal.valueOf(-1)));
    }
}
