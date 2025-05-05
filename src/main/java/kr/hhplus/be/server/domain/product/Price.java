package kr.hhplus.be.server.domain.product;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

@Embeddable
public class Price {

    @Column(name = "price", nullable = false)
    private BigDecimal amount;

    protected Price() {}

    private Price(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격은 음수일 수 없습니다.");
        }
        this.amount = amount;
    }

    public static Price of(BigDecimal amount) {
        return new Price(amount);
    }

    public Price add(Price additionalPrice) {
        return new Price(this.amount.add(additionalPrice.amount));
    }

    public Price subtract(Price subtractPrice) {
        return new Price(this.amount.subtract(subtractPrice.amount));
    }

    public Price update(Price price) {
        return new Price(price.getAmount());
    }

    public boolean isGreaterThan(Price target) {
        return this.amount.compareTo(target.getAmount()) > 0;
    }

    public BigDecimal getAmount() {
        return amount;
    }

}
