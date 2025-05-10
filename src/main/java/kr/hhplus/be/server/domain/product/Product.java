package kr.hhplus.be.server.domain.product;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "price"))
    private Price price;

    @Embedded
    @AttributeOverride(name = "quantity", column = @Column(name = "stock"))
    private Stock stock;

    @Builder
    private Product(Long id, String name, BigDecimal price, Integer stock) {
        if (name == null || price == null || stock == null) {
            throw new IllegalArgumentException("상품 이름, 가격, 재고는 필수입니다.");
        }
        this.id = id;
        this.name = name;
        this.price = Price.of(price);
        this.stock = Stock.of(stock);
    }

    public static Product create(String name, BigDecimal price, Integer stock) {
        return Product.builder()
                .name(name)
                .price(price)
                .stock(stock)
                .build();
    }

    public BigDecimal getAmount() {
        return this.price.getAmount();
    }

    public Integer getQuantity() {
        return this.stock.getQuantity();
    }

    public void decreaseStock(int quantity) {
        this.stock.decrease(quantity);
    }

    public void increaseStock(int quantity) {
        this.stock.increase(quantity);
    }

    public void validateOrder(BigDecimal expectedPrice, int orderQuantity) {
        if (this.price.getAmount().compareTo(expectedPrice) != 0) {
            throw new IllegalArgumentException("상품 가격이 일치하지 않습니다.");
        }

        if (this.stock.getQuantity() < orderQuantity) {
            throw new IllegalStateException("재고가 부족합니다.");
        }
    }

}
