package kr.hhplus.be.server.domain.order;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "item_id")
    private Item item;

    @Column
    private Integer price;

    @Column
    private Integer quantity;

    public static OrderItem createWithItemAndPriceAndQuantity(Item item, Integer price, Integer quantity) {
        return new OrderItem(item, price, quantity);
    }

    private OrderItem(Item item, Integer price, Integer quantity) {
        validateItem(item, price, quantity);

        this.item = item;
        this.price = price;
        this.quantity = quantity;
    }

    protected void setOrder(Order order){
        this.order = order;
    }

    public Long getItemId() {
        return item.getId();
    }

    public void deductItemQuantity() {
        this.item.decreaseStock(quantity);
    }

    public Integer calculatePrice(){
        return price * quantity;
    }

    public boolean belongsTo(Order order){
        return this.order.equals(order);
    }

    private static void validateItem(Item item, Integer price, Integer quantity) {
        if(Objects.isNull(item)) {
            throw new IllegalArgumentException("상품이 존재하지않습니다.");
        }

        if(!item.isSamePrice(price)) {
            throw new IllegalArgumentException("주문 상품의 가격이 일치하지않습니다.");
        }

        if(!item.hasEnoughStock(quantity)){
            throw new IllegalArgumentException("상품 재고가 부족합니다.");
        }
    }
}
