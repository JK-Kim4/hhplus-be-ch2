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
    private Price price;

    @Embedded
    private Stock stock;

    @Builder
    private Product(String name, BigDecimal price, Integer stock) {
        if (name == null || price == null || stock == null) {
            throw new IllegalArgumentException("상품 이름, 가격, 재고는 필수입니다.");
        }
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

}
