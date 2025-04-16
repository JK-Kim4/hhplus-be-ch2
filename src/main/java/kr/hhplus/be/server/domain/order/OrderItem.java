package kr.hhplus.be.server.domain.order;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id")
    private Item item;
    private Integer price;
    private Integer quantity;

    public OrderItem(Order order, Item item, Integer orderedPrice, Integer quantity) {
        if(order == null) {
            throw new IllegalArgumentException("주문 정보를 생성해주세요");
        }

        if (item == null) {
            throw new IllegalArgumentException("올바르지 않은 상품 정보입니다.");
        }

        if (!item.price().equals(orderedPrice)) {
            throw new IllegalArgumentException("가격 정보가 일치하지 않습니다.");
        }

        this.order = order;
        this.item = item;
        this.price = orderedPrice;
        this.quantity = quantity;
    }

    public OrderItem(Item item, Integer price, Integer quantity) {
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
}
