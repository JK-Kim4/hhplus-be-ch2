package kr.hhplus.be.server.domain.order;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.product.Price;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity @Getter
@Table(name = "order_item", indexes = {
        @Index(name = "idx_join_product_id", columnList = "product_id"),
        @Index(name = "idx_join_order_id", columnList = "order_id")})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Order order;

    @Embedded
    private Price price;

    private int quantity;

    protected OrderItem(Long productId, Price price, int quantity) {
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
    }

    public static OrderItem create(Long productId, Price price, int quantity) {
        return new OrderItem(productId, price, quantity);
    }

    public Price calculateAmount() {
        return this.price.multiply(this.quantity);
    }

    public void assignToOrder(Order order){
        this.order = order;
    }

    public BigDecimal getPrice() {
        return this.price.getAmount();
    }
}
