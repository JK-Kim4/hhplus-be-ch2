package kr.hhplus.be.server.domain.product;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class StockTest {

    @Test
    void 재고가_음수이면_예외를_던진다() {
        assertThatThrownBy(() -> Stock.of(-10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("재고 수량은 음수일 수 없습니다.");
    }

    @Test
    void 재고를_정상적으로_생성할_수_있다() {
        Stock stock = Stock.of(10);
        assertThat(stock.getQuantity()).isEqualTo(10);
    }

    @Test
    void 재고를_증가시킬_수_있다() {
        Stock stock = Stock.of(10);
        stock.increase(5);
        assertThat(stock.getQuantity()).isEqualTo(15);
    }

    @Test
    void 재고를_감소시킬_수_있다() {
        Stock stock = Stock.of(10);
        stock.decrease(3);
        assertThat(stock.getQuantity()).isEqualTo(7);
    }

    @Test
    void 재고보다_더많이_감소시키면_예외를_던진다() {
        Stock stock = Stock.of(5);
        assertThatThrownBy(() -> stock.decrease(10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("재고가 부족합니다.");
    }
}
