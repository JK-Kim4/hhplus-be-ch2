package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    private Long id;
    private Order order;
    private Long itemId;
    private Item item;
    private Integer price;
    private Integer quantity;

    public static OrderItem of(Long itemId, Integer price, Integer quantity) {
        return new OrderItem(itemId, price, quantity);
    }

    public OrderItem(Order order, Item item, Integer orderedPrice, Integer quantity) {
        if(order == null) {
            throw new IllegalArgumentException("주문 정보를 생성해주세요");
        }

        if (item == null) {
            throw new IllegalArgumentException("올바르지 않은 상품 정보입니다.");
        }

        if (!item.getPrice().equals(orderedPrice)) {
            throw new IllegalArgumentException("가격 정보가 일치하지 않습니다.");
        }

        this.order = order;
        this.item = item;
        this.price = orderedPrice;
        this.quantity = quantity;
    }

    public OrderItem(Long itemId, Integer price, Integer quantity) {
        this.itemId = itemId;
        this.price = price;
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
