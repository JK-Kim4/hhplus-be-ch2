package kr.hhplus.be.server.domain.order.orderItem;

import kr.hhplus.be.server.domain.item.Item;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.interfaces.exception.NotEnoughStockException;

public class OrderItem {

    private Long id;
    private Order order;
    private Item item;
    private Integer quantity;

    public Long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public Item getItem() {
        return item;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public OrderItem() {}

    public OrderItem(Order order, Item item, Integer quantity) {
        this.order = order;
        this.item = item;
        this.quantity = quantity;
    }

    public OrderItem(Long id, Order order, Item item, Integer quantity) {
        this.id = id;
        this.order = order;
        this.item = item;
        this.quantity = quantity;
    }

    public Integer calculatePrice() {
        if(!item.hasEnoughStock(this.quantity)){
            throw new NotEnoughStockException(NotEnoughStockException.NOT_ENOUGH_STOCK);
        }

        return this.item.getPrice() * this.quantity;
    }

    public void decreaseItemStock() {
        this.item.decreaseStock(this.quantity);
    }
}
