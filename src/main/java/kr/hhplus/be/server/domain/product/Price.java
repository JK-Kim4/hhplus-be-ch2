package kr.hhplus.be.server.domain.product;

import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

@Embeddable
public class Price {

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


    public Price multiply(int quantity) {
        return new Price(this.amount.multiply(BigDecimal.valueOf(quantity)));
    }

    public Price add(Price additionalPrice) {
        return new Price(this.amount.add(additionalPrice.amount));
    }

    public Price subtract(Price subtractPrice) {
        return new Price(this.amount.subtract(subtractPrice.amount));
    }

    public Price subtractDiscountAmount(BigDecimal discountAmount) {
        BigDecimal finalAmount = this.amount.subtract(discountAmount);
        if(finalAmount.compareTo(BigDecimal.ZERO) < 0){
            return Price.ZERO;
        }else{
            return new Price(finalAmount);
        }
    }

    public Price update(Price price) {
        return new Price(price.getAmount());
    }

    public boolean isGreaterThan(Price target) {
        return this.amount.compareTo(target.getAmount()) > 0;
    }

    public boolean isZero() {
        return this.amount.compareTo(BigDecimal.ZERO) == 0;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public static final Price ZERO = new Price(BigDecimal.ZERO);

}
