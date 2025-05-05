package kr.hhplus.be.server.domain.product;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Stock {

    @Column(name = "stock", nullable = false)
    private Integer quantity;

    protected Stock() {}

    private Stock(Integer quantity) {
        if (quantity == null || quantity < 0) {
            throw new IllegalArgumentException("재고 수량은 음수일 수 없습니다.");
        }
        this.quantity = quantity;
    }

    public static Stock of(Integer quantity) {
        return new Stock(quantity);
    }

    public void increase(int amount) {
        this.quantity += amount;
    }

    public void decrease(int amount) {
        if (this.quantity < amount) {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }
        this.quantity -= amount;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
