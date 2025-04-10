package kr.hhplus.be.server.domain.order.command;

import kr.hhplus.be.server.application.order.OrderItemCriteria;
import kr.hhplus.be.server.domain.item.Item;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.orderItem.OrderItem;
import kr.hhplus.be.server.interfaces.exception.InvalidPriceException;
import kr.hhplus.be.server.interfaces.exception.NotEnoughStockException;

import java.util.List;

public class OrderItemCreateCommand {


    private Long itemId;
    private Integer price;
    private Integer quantity;

    public OrderItemCreateCommand() {}

    public OrderItemCreateCommand(Long itemId, Integer price, Integer quantity) {
        this.itemId = itemId;
        this.price = price;
        this.quantity = quantity;
    }

    public static OrderItemCreateCommand from(OrderItemCriteria criteria) {
        return new OrderItemCreateCommand(criteria.getItemId(), criteria.getPrice(), criteria.getQuantity());
    }

    public Long getItemId() {return itemId;}

    public Integer getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public OrderItem toEntity(Order order, Item item, Integer quantity) {

        if(!item.getPrice().equals(this.getPrice())){
            throw new InvalidPriceException(String.format(InvalidPriceException.ITEM_PRICE_MISMATCH, this.getItemId()));
        }

        if(!item.hasEnoughStock(this.getQuantity())){
            throw new NotEnoughStockException(NotEnoughStockException.NOT_ENOUGH_STOCK);
        }

        return new OrderItem(order, item, quantity);
    }

    static public class Response{

        private List<OrderItem> orderItems;

        public List<OrderItem> getOrderItems() {
            return orderItems;
        }

        public Response(List<OrderItem> orderItems) {
            this.orderItems = orderItems;
        }

    }
}
