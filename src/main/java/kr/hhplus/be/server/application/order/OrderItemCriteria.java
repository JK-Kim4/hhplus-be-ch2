package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.item.ItemCommand;
import kr.hhplus.be.server.domain.order.command.OrderCommand;

public class OrderItemCriteria {

    private Long itemId;
    private Integer price;
    private Integer quantity;

    public static OrderItemCriteria of(Long itemId, Integer price, Integer quantity) {
        return new OrderItemCriteria(itemId, price, quantity);
    }

    public OrderCommand.OrderItemCreate toCommand(){
        return new OrderCommand.OrderItemCreate(itemId, price, quantity);
    }

    public ItemCommand.DecreaseStock toDecreaseStockCommand(){
        return new ItemCommand.DecreaseStock(itemId, quantity);
    }

    private OrderItemCriteria(){}

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

    public ItemCommand.CanOrder toCanOrderCommand() {
        return new ItemCommand.CanOrder(itemId, price, quantity);
    }
}
