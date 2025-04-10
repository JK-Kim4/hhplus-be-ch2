package kr.hhplus.be.server.application.order;

public class OrderItemCriteria {

    private Long itemId;
    private Integer price;
    private Integer quantity;

    public OrderItemCriteria(Long itemId, Integer price, Integer quantity) {
        this.itemId = itemId;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getItemId() {
        return itemId;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
